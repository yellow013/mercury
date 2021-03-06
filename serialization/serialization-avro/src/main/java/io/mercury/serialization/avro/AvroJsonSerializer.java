package io.mercury.serialization.avro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;

import io.mercury.common.character.Charsets;
import io.mercury.common.log.CommonLoggerFactory;
import io.mercury.common.serialization.spec.StringSerializer;

@NotThreadSafe
public class AvroJsonSerializer<T extends SpecificRecord> implements StringSerializer<T> {

	private static final Logger log = CommonLoggerFactory.getLogger(AvroJsonSerializer.class);

	private JsonEncoder encoder;

	private ByteArrayOutputStream outputStream;

	private DatumWriter<T> writer;

	/**
	 * Use default ByteArrayOutputStream size
	 */
	public AvroJsonSerializer(Class<T> recordClass) {
		this(recordClass, 8192);
	}

	public AvroJsonSerializer(Class<T> recordClass, int size) {
		this.writer = new SpecificDatumWriter<>(recordClass);
		this.outputStream = new ByteArrayOutputStream(size);
	}

	@Override
	public String serialization(T obj) {
		try {
			// TODO 对象可重用
			encoder = EncoderFactory.get().jsonEncoder(obj.getSchema(), outputStream);

			writer.write(obj, encoder);
			encoder.flush();
			byte[] bytes = outputStream.toByteArray();
			outputStream.reset();
			return new String(bytes, Charsets.UTF8);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			IOUtils.closeQuietly(outputStream);
			throw new RuntimeException("AvroTextSerializer serialization exception -> " + e.getMessage());
		}

	}

}
