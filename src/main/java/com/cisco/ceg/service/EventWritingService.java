package com.cisco.ceg.service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cisco.ceg.model.CEGEvent;
import com.cisco.ceg.processor.BatchProcessor;
import com.cisco.ceg.processor.EventConsumer;

/**
 * Service class which trigger threads for concurrent processing of events
 * 
 * @author surya
 *
 */
@Service
public class EventWritingService {

	private static final Logger LOG = LoggerFactory.getLogger(EventWritingService.class);

	@Autowired
	private BatchProcessor batchProcessor;

	@Autowired
	private ExecutorService executor;

	@Autowired
	@Qualifier("eventType1Consumer")
	private EventConsumer eventType1Consumer;

	@Autowired
	@Qualifier("eventType2Consumer")
	private EventConsumer eventType2Consumer;

	@Autowired
	@Qualifier("eventType3Consumer")
	private EventConsumer eventType3Consumer;

	@Autowired
	@Qualifier("eventType4Consumer")
	private EventConsumer eventType4Consumer;

	public void processEvents(List<CEGEvent> events) {

		Supplier<Stream<CEGEvent>> eventStreamSupplier = () -> events.parallelStream();

		LOG.debug("type 1 messages:"
				+ eventStreamSupplier.get().filter(ev -> ev.getEventType() == 1).collect(Collectors.toList()));

		LOG.debug("type 2 messages:"
				+ eventStreamSupplier.get().filter(ev -> ev.getEventType() == 2).collect(Collectors.toList()));
		
		LOG.debug("type 3 messages:"
				+ eventStreamSupplier.get().filter(ev -> ev.getEventType() == 3).collect(Collectors.toList()));

		LOG.debug("type 4 messages:"
				+ eventStreamSupplier.get().filter(ev -> ev.getEventType() == 4).collect(Collectors.toList()));

		eventStreamSupplier.get().filter(ev -> ev.getEventType() == 1)
				.forEach(e -> batchProcessor.getEventType1Queue().offer(e));
		executor.execute(eventType1Consumer);

		eventStreamSupplier.get().filter(ev -> ev.getEventType() == 2)
				.forEach(e -> batchProcessor.getEventType2Queue().offer(e));
		executor.execute(eventType2Consumer);

		eventStreamSupplier.get().filter(ev -> ev.getEventType() == 3)
				.forEach(e -> batchProcessor.getEventType3Queue().offer(e));
		executor.execute(eventType3Consumer);

		eventStreamSupplier.get().filter(ev -> ev.getEventType() == 4)
				.forEach(e -> batchProcessor.getEventType4Queue().offer(e));
		executor.execute(eventType4Consumer);

	}
}
