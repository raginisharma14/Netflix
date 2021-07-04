package com.netflix.model;
import java.util.List;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@Component
// Created request scope as there would be specific response for each request.
@Scope(WebApplicationContext.SCOPE_REQUEST)
// Do not populate null values in the response
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompatibleStreamServiceResponse{

	// used json property annotation for deserialization using jackson
	@JsonProperty
	List<Stream> streams;
	@JsonProperty
	String error;
	@JsonProperty
	Boolean success;
	@JsonProperty
	Boolean isValidMember;
	@JsonProperty
	String message;
	
	public List<Stream> getStreams() {
		return streams;
	}
	public void setStreams(List<Stream> streams) {
		this.streams = streams;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public Boolean getSuccess() {
		return success;
	}
	public Boolean getIsValidMember() {
		return isValidMember;
	}
	public void setIsValidMember(Boolean isValidMember) {
		this.isValidMember = isValidMember;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	
}
