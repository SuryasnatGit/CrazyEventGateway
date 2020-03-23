package com.cisco.ceg.ratelimiter;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sliding window rate limiter implementation.
 * 
 * TODO: one rate limiter is instantiated per client.
 */
public class GatewayRateLimiter implements RateLimiter {

	private static final Logger LOG = LoggerFactory.getLogger(GatewayRateLimiter.class);

	private int maxReqPerMin;
	private int timeWindow;

	public GatewayRateLimiter(final int maxReqPerMin, final int timeWindow) {
		this.maxReqPerMin = maxReqPerMin;
		this.timeWindow = timeWindow;
	}

	// key = time stamp , value = count of requests per hour. this can be extended to use Redis
	private final ConcurrentMap<Long, AtomicInteger> timeWindowMap = new ConcurrentHashMap<Long, AtomicInteger>();
	
	@Override
	public boolean allow() {
		
		LOG.debug("maxReqPerMin in " + this.getClass().getName() + " :" + maxReqPerMin);
		LOG.debug("timeWindow in " + this.getClass().getName() + " :" + timeWindow);

		long currTimeInMs = System.currentTimeMillis();
		long currTimeInMins = TimeUnit.MILLISECONDS.toMinutes(currTimeInMs); // currTime in ms to minute
		timeWindowMap.putIfAbsent(currTimeInMins, new AtomicInteger(0));

		LOG.debug("timeWindowMap :" + timeWindowMap);

		long prevTimeKey = currTimeInMins - timeWindow; // get the time 1 hr back from the current occurrence
		AtomicInteger prevTimeCount = timeWindowMap.get(prevTimeKey);
		if (prevTimeCount == null) {
			return timeWindowMap.get(currTimeInMins).incrementAndGet() <= maxReqPerMin;
		}

		int currMin = Calendar.getInstance().get(Calendar.MINUTE);
		long count = prevTimeCount.get() * ((60 - currMin) / 60) + timeWindowMap.get(currTimeInMins).incrementAndGet();
		LOG.debug("count :" + count);

		return count <= maxReqPerMin;
	}
}
