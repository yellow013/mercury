package io.mercury.common.collections.list;

import java.util.function.LongConsumer;

import javax.annotation.concurrent.NotThreadSafe;

import org.eclipse.collections.api.list.primitive.ImmutableLongList;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;

/**
 * 
 * 内部包含Long原始类型的数组, 长度不可变
 * 
 * @author yellow013
 *
 */
@NotThreadSafe
public final class LongSlidingWindow {

	private final int capacity;
	private final MutableLongList list;

	private int tail = -1;
	private int count = 0;

	private boolean isEmpty = true;
	private boolean isFull = false;

	public LongSlidingWindow(int capacity) {
		this.capacity = capacity;
		this.list = new LongArrayList(new long[capacity]);
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return isEmpty;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFull() {
		return isFull;
	}

	/**
	 * 
	 * @return
	 */
	public long tail() {
		if (isEmpty)
			return 0L;
		return list.get(tail);
	}

	/**
	 * 
	 * @return
	 */
	public long head() {
		if (isEmpty)
			return 0L;
		if (isFull)
			return list.get(tail + 1 == capacity ? 0 : tail + 1);
		return list.get(tail - count + 1);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public LongSlidingWindow add(long value) {
		addTail(value);
		return this;
	}

	/**
	 * 
	 * @param value
	 */
	private void addTail(long value) {
		// 如果尾部索引自增后等于容量, 重置尾部索引为0
		if (++tail == capacity)
			tail = 0;
		updateStatus();
		list.set(tail, value);
	}

	/**
	 * 更新窗口的当前状态
	 */
	private void updateStatus() {
		if (!isFull) {
			// 如果窗口容量未满时
			if (++count == capacity)
				isFull = true;
		}
		if (isEmpty) {
			isEmpty = false;
		}
	}

	/**
	 * 
	 * @param consumer
	 */
	public void each(LongConsumer consumer) {
		list.each(value -> consumer.accept(value));
	}

	/**
	 * 
	 * @return
	 */
	public int count() {
		return count;
	}

	/**
	 * 
	 * @return
	 */
	public long sum() {
		return list.sum();
	}

	/**
	 * 
	 * @return
	 */
	public long max() {
		return list.max();
	}

	/**
	 * 
	 * @return
	 */
	public long min() {
		return list.min();
	}

	/**
	 * 
	 * @return
	 */
	public long average() {
		return (long) list.average();
	}

	/**
	 * 
	 * @return
	 */
	public long median() {
		return (long) list.median();
	}

	/**
	 * 
	 * @return
	 */
	public ImmutableLongList snapshot() {
		return list.toImmutable();
	}

	public static void main(String[] args) {

		LongSlidingWindow window = new LongSlidingWindow(10);

		for (int i = 1; i < 30; i++) {
			System.out.println("head -> " + window.head());
			System.out.println("tail -> " + window.tail());
			System.out.println("count -> " + window.count);
			System.out.println("isEmpty == " + window.isEmpty() + " , isFull == " + window.isFull());
			window.add(i);
			System.out.println("head -> " + window.head());
			System.out.println("tail -> " + window.tail());
			System.out.println("count -> " + window.count);
			System.out.println("isEmpty == " + window.isEmpty() + " , isFull == " + window.isFull());
			System.out.print("values -> ");
			window.each(value -> System.out.print(value + " , "));
			System.out.println();
			System.out.println();
		}

	}

}
