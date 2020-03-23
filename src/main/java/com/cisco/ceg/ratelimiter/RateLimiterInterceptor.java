package com.cisco.ceg.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cisco.ceg.web.controller.CEGController;

/**
 * Spring based intercepter for handling rate limiting of incoming requests
 * 
 * @author surya
 *
 */
@Component
public class RateLimiterInterceptor extends HandlerInterceptorAdapter{

	private static final Logger LOG = LoggerFactory.getLogger(RateLimiterInterceptor.class);

	// maintain a map of rate limiter per client
	private ConcurrentMap<String, GatewayRateLimiter> rateLimiters = new ConcurrentHashMap<>();

	private int rateLimit;
	private int timeWindow;
	private boolean enabled;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		LOG.debug("Object handler :" + handler);

		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;

			if (handlerMethod.getBean() instanceof CEGController) {
				LOG.debug("Is Rate limiter enabled :" + enabled);
				LOG.debug("Max req per min :" + rateLimit);

				if (!enabled) {
					return true;
				}

				String clientId = request.getHeader("ClientID");
				LOG.debug("Client ID :" + clientId);
				if (clientId == null) {
					return true;
				}

				GatewayRateLimiter rateLimiter = getRateLimiter(clientId);
				boolean isAllowed = rateLimiter.allow();
				if (!isAllowed) {
					LOG.debug("Request not fulfilled due to too many requests");
					response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
					response.addHeader("X-RateLimit-Limit", Integer.toString(rateLimit));
				}

				LOG.debug("Is rate limit allowed :" + isAllowed);
				return isAllowed;
			}
		}

		return false;
	}

	private GatewayRateLimiter getRateLimiter(String clientId) {
		if (rateLimiters.containsKey(clientId)) {
			LOG.debug("rateLimiters map:" + rateLimiters);
			return rateLimiters.get(clientId);
		} else {
			// perform double checking
			synchronized (clientId) {
				if (rateLimiters.containsKey(clientId)) {
					return rateLimiters.get(clientId);
				}
				GatewayRateLimiter rateLimiter = new GatewayRateLimiter(rateLimit, timeWindow);
				rateLimiters.put(clientId, rateLimiter);
				return rateLimiter;
			}
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setRateLimit(int rateLimit) {
		this.rateLimit = rateLimit;
	}

	public void setTimeWindow(int timeWindow) {
		this.timeWindow = timeWindow;
	}
}
