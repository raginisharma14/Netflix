package com.netflix.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class MemberServiceResponse {
	
	@JsonProperty
	boolean success;
	@JsonProperty
	String error;
	@JsonProperty
	boolean isValidMember;
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
	public boolean isValidMember() {
		return isValidMember;
	}
	public void setValidMember(boolean isValidMember) {
		this.isValidMember = isValidMember;
	}

}
