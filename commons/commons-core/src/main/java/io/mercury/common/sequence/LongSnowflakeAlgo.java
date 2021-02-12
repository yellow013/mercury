package io.mercury.common.sequence;

import static io.mercury.common.util.BitFormatter.longBinaryFormat;
import static java.lang.System.currentTimeMillis;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import io.mercury.common.datetime.TimeZone;
import io.mercury.common.util.HexUtil;

/**
 * 
 * 通过将63位正整数long类型拆分为三部分实现唯一序列 <br>
 * 时间戳 | 所有者(可以是某个业务或者分布式系统上的机器) | 自增序列<br>
 * 
 * <pre>
 * 0b|---------------EPOCH TIMESTAMP----------------|--OWNER---|-INCREMENT-|
 * 0b01111111 11111111 11111111 11111111 11111111 11111111 11111111 11111111
 * 
 * <pre>
 *
 * @author yellow013
 */
public final class LongSnowflakeAlgo {

	/**
	 * 
	 * @author yellow013
	 */
	public static class Bulider {

		private final ZonedDateTime baseline;

		private Bulider(ZonedDateTime baseline) {
			this.baseline = baseline;
		}

		public LongSnowflakeAlgo bulid() {
			return new LongSnowflakeAlgo(this);
		}

	}

	/**
	 * 
	 * @param baseline
	 * @return
	 */
	public static LongSnowflakeAlgo newAllocator(LocalDate baseline) {
		return new Bulider(ZonedDateTime.of(baseline, LocalTime.MIN, ZoneOffset.UTC)).bulid();
	}

	/**
	 * 
	 * @param baseline
	 * @param zoneId
	 * @return
	 */
	public static LongSnowflakeAlgo newAllocator(LocalDate baseline, ZoneId zoneId) {
		return new Bulider(ZonedDateTime.of(baseline, LocalTime.MIN, zoneId)).bulid();
	}

	/**
	 * 
	 * @param bulider
	 */
	private LongSnowflakeAlgo(Bulider bulider) {
		this.baselineEpoch = bulider.baseline.toEpochSecond();
	}

	// 开始时间截 (使用自己业务系统指定的时间)
	private final long baselineEpoch;

	// 自增位最多占用16位
	private final int increment_bit_limit = Byte.SIZE * 2;

	// 开始时间截 (这个用自己业务系统上线的时间)
	private final long twepoch = 1575365018000L;

	// 机器ID所占的位数
	private final long workerIdBits = 10L;

	// 支持的最大机器ID, 结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
	@SuppressWarnings("unused")
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

	// 序列在ID中占的位数
	private final long sequenceBits = 12L;

	// 机器ID向左移12位
	private final long ownerIdShift = sequenceBits;

	// 时间截向左移22位(10+12)
	private final long timestampLeftShift = sequenceBits + workerIdBits;

	// 生成序列的掩码, 这里为4095 (0xfff == 4095 == 0b111111111111)
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	// 工作机器ID(0~1024)
	private long ownerId;

	// 毫秒内序列(0~4095)
	private long sequence = 0L;

	// 上次生成ID的时间截
	private volatile long lastTimestamp = -1L;

	// ==============================Function==========================================
	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * 
	 * @return SnowflakeId
	 */
	public synchronized long nextSeq() throws ClockBackwardException {
		long timestamp = currentTimeMillis();

		// 如果当前时间小于上一次ID生成的时间戳, 说明系统时钟回退过这个时候应当抛出异常
		if (timestamp < lastTimestamp) {
			throw new ClockBackwardException(lastTimestamp - timestamp);
		}

		// 如果是同一时间生成的, 则进行毫秒内序列
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			// 毫秒内序列溢出
			if (sequence == 0) {
				// 阻塞到下一个毫秒, 获得新的时间戳
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		// 时间戳改变, 毫秒内序列重置
		else {
			sequence = 0L;
		}

		// 上次生成ID的时间截
		lastTimestamp = timestamp;

		// 移位并通过或运算拼到一起组成64位的ID
		return ((timestamp - twepoch) << timestampLeftShift) // 时间戳左移至高位
				| (ownerId << ownerIdShift) // 所有者ID左移至中间位
				| sequence; // 自增位
	}

	/**
	 * 阻塞到下一个毫秒, 直到获得新的时间戳
	 * 
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(final long lastTimestamp) {
		long timestamp;
		do {
			timestamp = currentTimeMillis();
		} while (timestamp <= lastTimestamp);
		return timestamp;
	}

	public static void main(String[] args) {

		ZonedDateTime baseline = ZonedDateTime.of(LocalDate.of(2016, 1, 1), LocalTime.MIN, TimeZone.UTC);

		long baseEpochMilli = baseline.toInstant().toEpochMilli();
		System.out.println(baseEpochMilli);
		System.out.println(System.currentTimeMillis() - baseEpochMilli);
		System.out.println(Short.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE);
		System.out.println(0b01111111_11111111_11111111_11111111);
		System.out.println(0b11111111_11111111);

		long l0 = 0b00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L;
		System.out.println(longBinaryFormat(l0));

		long l = -1 >>> (Long.SIZE - (Byte.SIZE * 3 + 2));
		System.out.println(HexUtil.toHexString(-1L));
		System.out.println(l + " -> " + longBinaryFormat(l));

		System.out.println(Byte.SIZE);

		System.out.println(longBinaryFormat(System.currentTimeMillis() - baseline.toInstant().toEpochMilli()));
		System.out.println(longBinaryFormat(l));
		System.out.println(l);
		System.out.println(longBinaryFormat(l0));
		System.out.println(l0);
		System.out.println(longBinaryFormat(Long.MAX_VALUE));

		System.out.println(3600 * 24 * 365L);
		System.out.println(l0 / (1000 * 3600 * 24 * 365L));

	}

}