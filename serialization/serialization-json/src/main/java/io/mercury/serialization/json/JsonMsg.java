package io.mercury.serialization.json;

import javax.annotation.Nullable;

public final class JsonMsg {

	private long sequence;
	private long epoch;
	private int envelope;
	private int version;
	private ContentType contentType;
	private String content;

	/**
	 * 
	 * @param json
	 * @return
	 */
	@Nullable
	public static JsonMsg fromJson(String json) {
		return JsonParser.toObject(json, JsonMsg.class);
	}

	public long getSequence() {
		return sequence;
	}

	public long getEpoch() {
		return epoch;
	}

	public int getEnvelope() {
		return envelope;
	}

	public int getVersion() {
		return version;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public String getContent() {
		return content;
	}

	public JsonMsg setSequence(long sequence) {
		this.sequence = sequence;
		return this;
	}

	public JsonMsg setEnvelope(int envelope) {
		this.envelope = envelope;
		return this;
	}

	public JsonMsg setVersion(int version) {
		this.version = version;
		return this;
	}

	public JsonMsg setContentType(ContentType contentType) {
		this.contentType = contentType;
		return this;
	}

	public JsonMsg setContent(String content) {
		this.content = content;
		return this;
	}

	public String toJson() {
		return JsonWrapper.toJson(this);
	}

	@Override
	public String toString() {
		return toJson();
	}

	public static enum ContentType {

		INT, LONG, DOUBLE, STRING,

		OBJECT, LIST, MAP,

	}

}
