package com.netflix.client;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.model.CDNServiceResponse;
import com.netflix.model.Stream;
import com.netflix.responsemapper.CSSResponseMapper;
import com.netflix.responsemapper.InMemoryCache;

import rx.Observable;

/*
 * 
 * Client for cdn service
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CdnServiceClient {

	
	@Autowired
	InMemoryCache<String, List<Stream>> cache ;
	
	
	Client client = ClientBuilder.newClient();
	// TODO: use WebClient instead of WebTarget

	private static String URI = "http://localhost:8082/stream/";
	private static ObjectMapper ob = new ObjectMapper();
	static {

		ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}
	private static final long SERVICE_TIMEOUT = 20;
	private static final Logger LOGGER = LoggerFactory.getLogger(CSSResponseMapper.class);
	// No of times service would be retried if there is any error.
	// TODO: differentiate retriable vs non retriable error codes
	private static final Integer RETRY_COUNT =2;
	
	public void getStreamsBasedOnMovieId(List<Stream> streamId) {
		
		streamId.parallelStream().forEach(y -> {
		
			// Observable would wait for 20ms for the CDNservice to respond.
			Observable
					.just(client.target(URI).path("{streamId}/cdnlocation").resolveTemplate("streamId", y.getStreamId())
							.request())
					.map(x -> x.get()).timeout(SERVICE_TIMEOUT, TimeUnit.MILLISECONDS)//.subscribeOn(Schedulers.io())
					.map(x -> x.readEntity(String.class)).map(x -> {
						try {
							return ob.readValue(x, CDNServiceResponse.class);
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						return null;
					}).retry(RETRY_COUNT).doOnNext(x -> {
						LOGGER.debug("CDNLocation primary Url" + x.getCdnLocation());
						y.setPrimaryUrl(x.getCdnLocation().getPrimaryUrl());
						y.setSecondaryUrl(x.getCdnLocation().getSecondaryUrl());

					})

					.doOnError(x ->{
					
					LOGGER.debug("Error in CDNService ::" + x.getMessage());
					
					})
					.doOnCompleted(() -> {
						LOGGER.debug("CDNService completed");
						
					}).subscribe();
					
					

		});
			
		
		

	}

}
