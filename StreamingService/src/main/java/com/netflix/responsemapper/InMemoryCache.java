package com.netflix.responsemapper;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/*
 * This class is used as an inmemory cache to avoid doing network calls for each request
 * if the response is already present in the cache. We can retrieve from here
 * 
 */
@Component
public class InMemoryCache<Key, Value> {

	private ConcurrentHashMap<Key, Value> cache = new ConcurrentHashMap<Key, Value>();

	public ConcurrentHashMap<Key, Value> getCache() {
		return cache;
	}

	public void setCache(ConcurrentHashMap<Key, Value> cache) {
		this.cache = cache;
	}

}
