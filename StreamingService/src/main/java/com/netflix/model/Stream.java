package com.netflix.model;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
//Created request scope as there would be specific response for each request.
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class Stream {
	
	// used json property annotation for deserialization using jackson
	@JsonProperty
	long streamId;
	@JsonProperty
	String streamType;
	@JsonProperty
	String streamEncode;
	@JsonProperty
	Integer streamBitrate;
	@JsonProperty
	String primaryUrl;
	@JsonProperty
	String secondaryUrl;
	
	public long getStreamId() {
		return streamId;
	}
	public void setStreamId(long streamId) {
		this.streamId = streamId;
	}
	public String getStreamType() {
		return streamType;
	}
	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}
	public String getStreamEncode() {
		return streamEncode;
	}
	public void setStreamEncode(String streamEncode) {
		this.streamEncode = streamEncode;
	}
	public Integer getStreamBitrate() {
		return streamBitrate;
	}
	public void setStreamBitrate(Integer streamBitrate) {
		this.streamBitrate = streamBitrate;
	}
	public String getPrimaryUrl() {
		return primaryUrl;
	}
	public void setPrimaryUrl(String primaryUrl) {
		this.primaryUrl = primaryUrl;
	}
	public String getSecondaryUrl() {
		return secondaryUrl;
	}
	public void setSecondaryUrl(String secondaryUrl) {
		this.secondaryUrl = secondaryUrl;
	}
	
	
	
	
}
