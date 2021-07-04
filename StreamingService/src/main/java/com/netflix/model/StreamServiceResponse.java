package com.netflix.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
//Created request scope as there would be specific response for each request.
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StreamServiceResponse {
	
	// used json property annotation for deserialization using jackson
	@JsonProperty
	boolean success;	
	@JsonProperty
	List<Stream> streams = new ArrayList<Stream>();
	@JsonProperty
	String error;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<Stream> getStreams() {
		return streams;
	}
	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}
	
	
}
