package com.cisco.ceg.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cisco.ceg.processor.BatchProcessor;
import com.cisco.ceg.processor.EventConsumer;
import com.cisco.ceg.processor.FileTranslator;
import com.cisco.ceg.processor.Log4JFilePersistor;
import com.cisco.ceg.processor.Persistor;
import com.cisco.ceg.processor.Translator;
import com.cisco.ceg.ratelimiter.RateLimiterInterceptor;

/**
 * Defines the spring beans used in the application
 * 
 * @author surya
 *
 */
@Configuration
public class CEGConfig implements WebMvcConfigurer {

//	private static final Logger LOG = LoggerFactory.getLogger(CEGConfig.class);

	/**
	 * Minimum number of threads used in Thread pool
	 */
	@Value("${ceg.core.pool.size}")
	private int corePoolSize;

	/**
	 * Maximum number of threads used in Thread pool
	 */
	@Value("${ceg.max.pool.size}")
	private int maxPoolSize;

	@Value("${ceg.keep.alive.time}")
	private int keepAliveTime;

	@Value("${ceg.blocking.queue.capacity}")
	private int blockingQueueCapacity;

	/**
	 * Flag to check if rate limiter is enabled or not
	 */
	@Value("${ceg.rate.limit.enabled}")
	private boolean enabled;

	/**
	 * Number of requests allowed by the rate limiter in a unit time
	 */
	@Value("${ceg.rate.limit}")
	private int rateLimit;

	/**
	 * time span for rate limiter
	 */
	@Value("${ceg.rate.limit.time.window}")
	private int timeWindow;

	@Bean
	public RateLimiterInterceptor createInterceptor() {
		RateLimiterInterceptor interceptor = new RateLimiterInterceptor();
		interceptor.setEnabled(enabled);
		interceptor.setRateLimit(rateLimit);
		interceptor.setTimeWindow(timeWindow);
		return interceptor;
	}

	@Bean
	public ExecutorService createThreadPoolExecutor() {
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(blockingQueueCapacity);
		ExecutorService executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
				workQueue);
		return executor;
	}

	@Bean
	public BatchProcessor createBatchProcessor() {
		return new BatchProcessor(blockingQueueCapacity);
	}

	@Bean(name = "eventType1Consumer")
	public EventConsumer createEventConsumerForEventType1() {
		return new EventConsumer(1);
	}

	@Bean(name = "eventType2Consumer")
	public EventConsumer createEventConsumerForEventType2() {
		return new EventConsumer(2);
	}

	@Bean(name = "eventType3Consumer")
	public EventConsumer createEventConsumerForEventType3() {
		return new EventConsumer(3);
	}

	@Bean(name = "eventType4Consumer")
	public EventConsumer createEventConsumerForEventType4() {
		return new EventConsumer(4);
	}

	@Bean
	public Persistor createLogFilePersistor() {
		return new Log4JFilePersistor<>();
	}

	@Bean
	@Scope("prototype")
	public Translator createFileTranslator() {
		return new FileTranslator();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(createInterceptor()).addPathPatterns("/**");
	}
}
