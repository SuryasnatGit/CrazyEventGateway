package com.cisco.ceg.processor;

import java.io.Serializable;

/**
 * Interface to persist message content to file
 * 
 * @author surya
 *
 */
public interface Persistor extends Cloneable, Serializable{

	/**
	 * Calls methods for opening log files for valid and invalid data
	 * @param transaction Transaction DTO
	 */
	public void openLogFile(Transaction transaction);
	
	/**
	 * This method rolls over the data/error files after the specified time period
	 */
	public void rolloutFile();

	/**
	 * This method cLoses the idle files associated with loggers that are not used for a 
	 * specified time interval
	 */
	public void closeIdleFiles();
	
	/**
	 * This method removes the logger from transactionLoggerMap which are more than 24 hours long 
	 * i.e no more in use. 
	 */
	public void purgeStaleLogger();
	
	/**
	 * This method is used for checking variable initialization
	 * @throws FilePersistorException 
	 */
	public void init();
}
