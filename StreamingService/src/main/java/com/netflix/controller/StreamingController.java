package com.netflix.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import com.netflix.handler.RequestHandler;
import com.netflix.model.CompatibleStreamServiceResponse;
/*
 * StreamingController will transfer the request to request handler.
 */

@RestController
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StreamingController{
 

	@Autowired
	RequestHandler requestHandler;
		
	@PostMapping("/compatibleStreamingData/")           
	public CompatibleStreamServiceResponse compatibleStreamingData(@RequestBody String request) {	
				
		return this.requestHandler.handleRequest(request);
		
	}


	
}
