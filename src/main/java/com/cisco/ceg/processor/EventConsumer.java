package com.cisco.ceg.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cisco.ceg.model.CEGEvent;

/**
 * Consumer class to drain events from the queue. This will run in a
 * multi-threaded fashion. Depending on the event type(1,2,3,4) each message is
 * read from the blocking queue. The checkDrainReady() method checks if either
 * the number of messages in the queue exceeds the drain limit or the time limit
 * of configured wait-time is reached(whichever is sooner). Based on this the
 * specific number of objects will be drained from the queue and sent further
 * for file persistence.
 * 
 * @author surya
 *
 */
public class EventConsumer implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(EventConsumer.class);

	@Value("${ceg.max.elements.drain}")
	private int maxElementsToDrain;

	@Value("${ceg.wait.time}")
	private int waitTime;

	private int eventType;

	@Autowired
	private BatchProcessor processor;

	@Autowired
	private Translator translator;

	public EventConsumer(int et) {
		this.eventType = et;
	}

	@Override
	public void run() {
		switch (eventType) {
		case 1:
			LinkedBlockingQueue<CEGEvent> eventType1Queue = processor.getEventType1Queue();
			while (!eventType1Queue.isEmpty()) {
				checkDrainReady(eventType1Queue);

				List<CEGEvent> eventsColl = drainEvents(eventType1Queue);

				LOG.debug("type 1 events :" + eventsColl);
				persistData(eventsColl, eventType);
			}
			break;
		case 2:
			LinkedBlockingQueue<CEGEvent> eventType2Queue = processor.getEventType2Queue();
			while (!eventType2Queue.isEmpty()) {
				checkDrainReady(eventType2Queue);

				List<CEGEvent> eventsColl = drainEvents(eventType2Queue);

				LOG.debug("type 2 events :" + eventsColl);
				persistData(eventsColl, eventType);
			}
			break;
		case 3:
			LinkedBlockingQueue<CEGEvent> eventType3Queue = processor.getEventType3Queue();
			while (!eventType3Queue.isEmpty()) {
				checkDrainReady(eventType3Queue);

				List<CEGEvent> eventsColl = drainEvents(eventType3Queue);

				LOG.debug("type 3 events :" + eventsColl);
				persistData(eventsColl, eventType);
			}
			break;
		case 4:
			LinkedBlockingQueue<CEGEvent> eventType4Queue = processor.getEventType4Queue();
			while (!eventType4Queue.isEmpty()) {
				checkDrainReady(eventType4Queue);

				List<CEGEvent> eventsColl = drainEvents(eventType4Queue);

				LOG.debug("type 4 events :" + eventsColl);
				persistData(eventsColl, eventType);
			}
			break;
		}
	}

	protected List<CEGEvent> drainEvents(LinkedBlockingQueue<CEGEvent> eventQueue) {
		List<CEGEvent> eventsColl = new ArrayList<>();
		eventQueue.drainTo(eventsColl, maxElementsToDrain);
		return eventsColl;
	}

	private void checkDrainReady(LinkedBlockingQueue<CEGEvent> eventQueue) {
		long startTime = System.currentTimeMillis() / 1000;
		LOG.debug("startTime :" + startTime);
		while (true) {
			long currTime = System.currentTimeMillis() / 1000;
			if (eventQueue.size() >= maxElementsToDrain || (currTime - startTime) == waitTime) {
				break;
			}
		}
		LOG.debug("draining to sink at time :" + System.currentTimeMillis() / 1000);
	}

	private void persistData(List<CEGEvent> eventsColl, int eventType) {
		Transaction transaction = new Transaction();
		transaction.setTransactionList(eventsColl);
		transaction.setEventType(eventType);

		try {
			translator.persistData(transaction);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

}
