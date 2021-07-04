package com.netflix.responsemapper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.netflix.client.CdnServiceClient;
import com.netflix.client.MemberServiceClient;
import com.netflix.client.StreamServiceClient;
import com.netflix.model.CompatibleStreamServiceRequest;
import com.netflix.model.CompatibleStreamServiceResponse;
import com.netflix.model.MemberServiceResponse;
import com.netflix.model.Stream;

import rx.Observable;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CSSResponseMapper {

	@Autowired
	MemberServiceClient memberServiceClient;

	@Autowired
	StreamServiceClient streamServiceClient;

	@Autowired
	CdnServiceClient cdnServiceClient;

	@Autowired
	CompatibleStreamServiceResponse response;
	
	private static final long SERVICE_TIMEOUT = 10000;


    private static final Logger LOGGER = LoggerFactory.getLogger(CSSResponseMapper.class); 
    
	public CompatibleStreamServiceResponse mapResponse(CompatibleStreamServiceRequest streamData) {

		Observable<MemberServiceResponse> memObservable = memberServiceClient.isMemberValid(streamData.getMemberId());
		
		Observable<List<Stream>> streamObservable = streamServiceClient
				.getStreamsBasedOnMovieId(streamData.getMovieId(), streamData.getMaxBitrate(), streamData.getEncodes());

		Observable.zip(memObservable, streamObservable,
				
			

				(x, y) -> combine(x, y)).timeout(SERVICE_TIMEOUT, TimeUnit.MILLISECONDS).toBlocking().subscribe(x -> {
					if (x == null) {

						this.response.setIsValidMember(false);
						this.response.setSuccess(true);
						this.response.setMessage("Not a valid member; cannot retrieve stream data for non members");

					} else {

						this.response.setStreams(x);
						this.response.setSuccess(true);

					}
				}, x -> {
					LOGGER.info("Error occured in either MemberService or StreamService");
					LOGGER.debug(x.getMessage());
					this.response = new CompatibleStreamServiceResponse();
					this.response.setError("error in downstream services");
					

				}, () -> LOGGER.info("CSSResponseMapper completed successfully"));

		if (this.response.getStreams() != null && !this.response.getStreams().isEmpty()) {
			cdnServiceClient.getStreamsBasedOnMovieId(this.response.getStreams());
		}
		
		return response;

	}

	private List<Stream> combine(MemberServiceResponse m, List<Stream> s) {
		
		
		if (m.isValidMember()) {
			LOGGER.debug("member is valid: fetching stream meta data");
			return s;
		}
		LOGGER.debug("member is not valid: not fetching stream meta data returning null");
		return null;
	}

}
