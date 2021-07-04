package com.netflix.handler;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import com.netflix.model.CompatibleStreamServiceRequest;
import com.netflix.model.CompatibleStreamServiceResponse;
import com.netflix.requestmapper.CSSRequestMapper;
import com.netflix.responsemapper.CSSResponseMapper;
/*
 * For every request calls downstream services , gets responses, does tranformation and return the final response
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RequestHandler {

	@Autowired
	CSSRequestMapper cssRequestMapper;
	
	
	@Autowired
	CSSResponseMapper cssResponseMapper;
	
	@Autowired
	CompatibleStreamServiceResponse cssResponse;	
	
	
	public CompatibleStreamServiceResponse handleRequest(String req) {

		Optional<CompatibleStreamServiceRequest> cssreq = cssRequestMapper.mapRequest(req);
		if(cssreq.isPresent()) {
			return cssResponseMapper.mapResponse(cssreq.get());
		}
		
		cssResponse.setError("Request Validation Failed");
		return cssResponse;	
	
		
		
	}
	
	
	
}
