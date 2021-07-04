package com.netflix.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonProperty;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CompatibleStreamServiceRequest {
	
	@JsonProperty("movieId")
	Integer movieId;
	@JsonProperty("memberId")
	String  memberId;
	@JsonProperty("maxBitrate")
	Integer maxBitrate;
	@JsonProperty("encodes")
	List<StreamEncodeEnum> encodes = new ArrayList<StreamEncodeEnum>();
	public Integer getMovieId() {
		return movieId;
	}
	public void setMovieId(Integer movieId) {
		this.movieId = movieId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Integer getMaxBitrate() {
		return maxBitrate;
	}
	public void setMaxBitrate(Integer maxBitrate) {
		this.maxBitrate = maxBitrate;
	}
	public List<StreamEncodeEnum> getEncodes() {
		return encodes;
	}
	public void setEncodes(List<StreamEncodeEnum> encodes) {
		this.encodes = encodes;
	}
	@Override
	public String toString() {
		return "CompatibleStreamServiceRequest [movieId=" + movieId + ", memberId=" + memberId + ", maxBitrate="
				+ maxBitrate + ", encodes=" + encodes + "]";
	}
	
	
}
