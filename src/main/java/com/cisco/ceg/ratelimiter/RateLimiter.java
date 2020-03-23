package com.cisco.ceg.ratelimiter;

/**
 * Generic rate limiter interface
 * 
 * @author surya
 *
 */
public interface RateLimiter {

	public boolean allow();
}
