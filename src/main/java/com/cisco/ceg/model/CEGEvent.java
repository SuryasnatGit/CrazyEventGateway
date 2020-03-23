package com.cisco.ceg.model;

/**
 * DTO class for containing event
 * 
 * @author surya
 *
 */
public final class CEGEvent {

	private int eventType;
	private Object data;
	
	public CEGEvent(int eventType, Object d) {
		this.eventType = eventType;
		this.data = d;
	}

	public int getEventType() {
		return eventType;
	}

	public Object getData() {
		return data;
	}

	@Override
	public String toString() {
		return "CEGEvent [eventType=" + eventType + ", data=" + data + "]";
	}
}
