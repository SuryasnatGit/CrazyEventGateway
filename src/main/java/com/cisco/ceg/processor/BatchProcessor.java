package com.cisco.ceg.processor;

import java.util.concurrent.LinkedBlockingQueue;

import com.cisco.ceg.model.CEGEvent;

/**
 * Container class having blocking queues for each sync type
 * 
 * @author surya
 *
 */
public class BatchProcessor {

//	private static final Logger LOG = LoggerFactory.getLogger(BatchProcessor.class);

	private LinkedBlockingQueue<CEGEvent> eventType1Queue;
	private LinkedBlockingQueue<CEGEvent> eventType2Queue;
	private LinkedBlockingQueue<CEGEvent> eventType3Queue;
	private LinkedBlockingQueue<CEGEvent> eventType4Queue;

	public BatchProcessor(final int capacity) {
		eventType1Queue = new LinkedBlockingQueue<>(capacity);
		eventType2Queue = new LinkedBlockingQueue<>(capacity);
		eventType3Queue = new LinkedBlockingQueue<>(capacity);
		eventType4Queue = new LinkedBlockingQueue<>(capacity);
	}

	public LinkedBlockingQueue<CEGEvent> getEventType1Queue() {
		return eventType1Queue;
	}

	public LinkedBlockingQueue<CEGEvent> getEventType2Queue() {
		return eventType2Queue;
	}

	public LinkedBlockingQueue<CEGEvent> getEventType3Queue() {
		return eventType3Queue;
	}

	public LinkedBlockingQueue<CEGEvent> getEventType4Queue() {
		return eventType4Queue;
	}
}
