package com.netflix.client;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
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
import com.netflix.model.MemberServiceResponse;
import com.netflix.responsemapper.CSSResponseMapper;
import com.netflix.responsemapper.InMemoryCache;

import rx.Observable;
import rx.schedulers.Schedulers;

/*
 * 
 * Client for member service
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class MemberServiceClient {

	//Retrieve validity of member from cache if present else call member service.
	
	@Autowired
	InMemoryCache<String, MemberServiceResponse> cache ;
	Client client = ClientBuilder.newClient();

	// TODO: can use WebClient
	private static String URI = "http://localhost:8084/member/";
	private static ObjectMapper ob = new ObjectMapper();
	static {

		ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}
	private static final long SERVICE_TIMEOUT = 50;
	// No of times service would be retried if there is any error.
		// TODO: differentiate retriable vs non retriable error codes
	private static final Integer RETRY_COUNT =2;
	private static final Logger LOGGER = LoggerFactory.getLogger(CSSResponseMapper.class);

	public Observable<MemberServiceResponse> isMemberValid(String memberId) {
		WebTarget webTarget = client.target(URI);
		WebTarget ur = webTarget.path("/{memberId}/status").resolveTemplate("memberId", memberId);
		
		/*
		 * Retrieve from cache if present else do a network call to check if member is valid
		 */
		if(cache.getCache().containsKey(memberId)) {
			
			return Observable.just(cache.getCache().get(memberId));
			
		}
		// Observable would wait for 20ms for the member service to respond.
		return Observable.just(ur.request()).map(x -> x.get()).timeout(SERVICE_TIMEOUT, TimeUnit.MILLISECONDS)
				.subscribeOn(Schedulers.io()).map(x -> x.readEntity(String.class)).map(x -> {

					try {
						return ob.readValue(x, MemberServiceResponse.class);
					} catch (JsonMappingException e) {						
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}

					MemberServiceResponse msr = new MemberServiceResponse();
					msr.setError("error in parsing the response");
					return msr;

				})
				.retry(RETRY_COUNT)
				.doOnNext(x -> {
					LOGGER.debug("Print isValidMember:: " + x.isValidMember());
					LOGGER.debug("Thread executing member service" + Thread.currentThread().getName());
					cache.getCache().put((String)memberId, x);

				}).doOnError(x -> {

					if (x instanceof TimeoutException) {
						LOGGER.info("Timeout connecting to member service" + x.getMessage());
						
					} else {
						LOGGER.info("unknown error connecting to member service" + x.getMessage());
					}
					client.close();
				}).doOnCompleted(() -> {

					LOGGER.debug("Memberservice completed" + Thread.currentThread().getName());
					client.close();
				});

	}

}
