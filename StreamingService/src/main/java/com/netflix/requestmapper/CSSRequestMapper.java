package com.netflix.requestmapper;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.model.CompatibleStreamServiceRequest;
import com.netflix.responsemapper.CSSResponseMapper;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CSSRequestMapper {

	@Autowired
	CSSResponseMapper cssResponseMappper;
	private static ObjectMapper ob = new ObjectMapper();
	static {
		
		ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	public Optional<CompatibleStreamServiceRequest> mapRequest(String req) {
	
		CompatibleStreamServiceRequest streamData = null;
		try {
			streamData = ob.readValue(req, CompatibleStreamServiceRequest.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return Optional.ofNullable(streamData);
		
	}

}
