package com.cisco.ceg.processor;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * For translating consumed messages from queue and forward for file storage
 *
 */
public class FileTranslator implements Translator {
	
	private static final long serialVersionUID = -5002490659893746757L;

	@Autowired
	private Persistor log4jFilePersistor;

	/**
	 * Processes the transaction object to persist the data into file
	 * @param transaction Transaction object
	 */
	public void persistData(Transaction transaction) throws Exception{
		log4jFilePersistor.openLogFile(transaction);
	}
}
