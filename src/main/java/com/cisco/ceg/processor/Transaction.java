package com.cisco.ceg.processor;

import java.util.List;

import com.cisco.ceg.model.CEGEvent;

/**
 * 
 * Transaction DTO
 *
 */
public class Transaction {

	private Integer eventType = null;

	private List<CEGEvent> transactionList = null;

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the transactionList
	 */
	public List<CEGEvent> getTransactionList() {
		return transactionList;
	}

	/**
	 * @param transactionList the transactionList to set
	 */
	public void setTransactionList(List<CEGEvent> transactionList) {
		this.transactionList = transactionList;
	}

}
