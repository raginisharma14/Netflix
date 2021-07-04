package com.netflix.model;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
/*
 * 
 * Data model for CDNservice
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CDNLocation {

	String primaryUrl;
	String secondaryUrl;
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
