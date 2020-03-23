package com.cisco.ceg;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cisco.ceg.ratelimiter.GatewayRateLimiter;

@SpringBootTest
class CEGRateLimiterTests {

	@Test
	void testRateLimitNormal() {
		GatewayRateLimiter rateLimiter = new GatewayRateLimiter(2, 60000);
		// 1st time and 2nd time it will allow as we have mentioned limit of 2 per min
		assertTrue(rateLimiter.allow());
		assertTrue(rateLimiter.allow());
	}

	@Test
	void testRateLimitExceeded() {
		GatewayRateLimiter rateLimiter = new GatewayRateLimiter(2, 60000);
		// 1st time and 2nd time it will allow as we have mentioned limit of 2 per min
		assertTrue(rateLimiter.allow());
		assertTrue(rateLimiter.allow());
		// 3rd time it will not allow
		assertFalse(rateLimiter.allow());
	}
}
