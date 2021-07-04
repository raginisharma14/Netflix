package com.netflix.model;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
//Created request scope as there would be specific response for each request.
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CDNServiceResponse {

	// used json property annotation for deserialization using jackson
	@JsonProperty
	boolean success;
	@JsonProperty
	CDNLocation cdnLocation;
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
	public CDNLocation getCdnLocation() {
		return cdnLocation;
	}
	public void setCdnLocation(CDNLocation cdnLocation) {
		this.cdnLocation = cdnLocation;
	}
	@Override
	public String toString() {
		return "CDNServiceResponse [success=" + success + ", cdnLocation=" + cdnLocation + ", error=" + error + "]";
	}

	
	
}
