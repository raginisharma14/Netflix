package com.netflix.client;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
import com.netflix.model.StreamServiceResponse;
import com.netflix.responsemapper.CSSResponseMapper;
import com.netflix.responsemapper.InMemoryCache;
import com.netflix.model.Stream;
import com.netflix.model.StreamEncodeEnum;

import rx.Observable;
import rx.schedulers.Schedulers;
/*
 * 
 * Client for the streamService
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StreamServiceClient {

	// Retrieve streams from cache if present else call streamservice.
	@Autowired
	InMemoryCache<Integer, List<Stream>> cache;

	Client client = ClientBuilder.newClient();
	// TODO: use WebClient instead of WebTarget

	private static String URI = "http://localhost:8088/movie/";
	private static ObjectMapper ob = new ObjectMapper();
	static {

		ob.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}
	//Timeout set for the thread waiting on stream service after which it would timeout.
	private static final long SERVICE_TIMEOUT = 50;
	private static final Logger LOGGER = LoggerFactory.getLogger(CSSResponseMapper.class);
	// No of times service would be retried if there is any error.
	// TODO: differentiate retriable vs non retriable error codes
	private static final Integer RETRY_COUNT = 2;

	public Observable<List<Stream>> getStreamsBasedOnMovieId(Integer movieId, Integer maxBitrate,
			List<StreamEncodeEnum> listOfStreams) {
		WebTarget wb = client.target(URI).path("{movieId}/streams").resolveTemplate("movieId", movieId);
		/*
		 * Retrieve from cache if present else do a network call to retrieve the streams
		 */
		if (cache.getCache().contains(movieId)) {

			return Observable.just(cache.getCache().get(movieId));
		}
		// Observable would wait for a maximum of 20ms for the Streamservice to respond.

		return Observable.just(wb.request()).map(x -> x.get()).timeout(SERVICE_TIMEOUT, TimeUnit.MILLISECONDS)
				.subscribeOn(Schedulers.io()).map(x -> x.readEntity(String.class)).map(x -> {
					try {
						return ob.readValue(x, StreamServiceResponse.class);
					} catch (JsonMappingException e) {
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
					return null;
				}).observeOn(Schedulers.computation()).map(x -> x.getStreams())
				.map(x -> x.stream()
						.filter(x1 -> x1.getStreamBitrate() <= maxBitrate
								&& listOfStreams.contains(StreamEncodeEnum.valueOf(x1.getStreamEncode()))))
				.map(x -> x.collect(Collectors.toList())).retry(RETRY_COUNT).doOnNext(x -> {
					LOGGER.debug(
							"On next of stream service executing thred" + "Thread" + Thread.currentThread().getName());
					cache.getCache().put(movieId, x);
				})

				.doOnError(x -> {
					LOGGER.debug("Error in stream service::" + x.getMessage());
					
					client.close();
				}).doOnCompleted(() -> {
					LOGGER.debug("stream service completed");
					client.close();
				});
				
	}

}
