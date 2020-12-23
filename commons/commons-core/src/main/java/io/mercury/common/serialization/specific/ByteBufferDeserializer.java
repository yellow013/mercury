package io.mercury.common.serialization.specific;

import java.nio.ByteBuffer;

import io.mercury.common.serialization.Deserializer;

@FunctionalInterface
public interface ByteBufferDeserializer<R> extends Deserializer<ByteBuffer, R> {

}