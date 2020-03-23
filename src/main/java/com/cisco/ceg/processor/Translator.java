package com.cisco.ceg.processor;

import java.io.Serializable;

/**
 * Processes the transaction object to persist the data into sync(can be file,
 * kafka or something else..)
 * 
 * @author surya
 *
 */
public interface Translator extends Cloneable, Serializable{
	
	public void persistData(Transaction transaction) throws Exception;

}
