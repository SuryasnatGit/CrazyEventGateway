package com.cisco.ceg.processor;

import static com.cisco.ceg.common.CEGConstants.APPEND_DATE_SDF;
import static com.cisco.ceg.common.CEGConstants.ENCODER_PATTERN;
import static com.cisco.ceg.common.CEGConstants.FILE_EXTN;
import static com.cisco.ceg.common.CEGConstants.FILE_NAME_SDF;
import static com.cisco.ceg.common.CEGConstants.FILE_SIZE_LIMIT;
import static com.cisco.ceg.common.CEGConstants.ROLLING_FILE_NAME_PATTERN;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.cisco.ceg.model.CEGEvent;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * 
 * Persistor class to write the transaction contents to file.
 *
 * @param <E>
 */
public class Log4JFilePersistor<E> implements Persistor {

	private static final long serialVersionUID = -8670005827462032764L;

	private static final Logger LOG = LoggerFactory.getLogger(Log4JFilePersistor.class);

	@Value("${data.dir}")
	private String dataSir;

	/**
	 * The folder path where data will be persisted
	 */
	private String fileFolder;

	/**
	 * Atomic Reference to map of logger instances
	 */
	private AtomicReference<HashMap<String, ch.qos.logback.classic.Logger>> transactionLoggerMap = new AtomicReference<>();

	public Log4JFilePersistor() {
		transactionLoggerMap.set(new HashMap<String, ch.qos.logback.classic.Logger>());
	}

	/**
	 * Calls methods for opening log files for valid and invalid data
	 * 
	 * @param transaction Transaction DTO
	 */
	public void openLogFile(Transaction transaction) {

		ch.qos.logback.classic.Logger transactionLogger = null;

		List<CEGEvent> transactionListToPersist = transaction.getTransactionList();

		Date date = new Date();

		String fileNameToSet = "ET_" + transaction.getEventType();
		SimpleDateFormat sdfForFileName = new SimpleDateFormat(FILE_NAME_SDF);
		String appendDateForFileName = sdfForFileName.format(date.getTime());
		String fileNameValue = fileNameToSet + "_" + appendDateForFileName + FILE_EXTN;
		LOG.debug("Log4JFilePersistor::: Writing valid data to file :::" + fileNameValue);
		SimpleDateFormat sdf = new SimpleDateFormat(APPEND_DATE_SDF);
		String appendDate = sdf.format(date.getTime());
		fileNameToSet = appendDate + fileNameToSet;

		// First check whether a logger was created already in the hash map
		// Create a logger for the associated transaction
		if (transactionLoggerMap.get().containsKey(fileNameToSet)) {
			transactionLogger = transactionLoggerMap.get().get(fileNameToSet);
		} else {
			transactionLogger = createRollingLogger(fileNameValue);
			transactionLoggerMap.get().put(fileNameToSet, transactionLogger);
		}
		LOG.debug("transactionLoggerMap :" + transactionLoggerMap);

		// Finally log the data received into the appropriate folder and file using the
		// transaction logger created
		LOG.debug("transactionLogger instance :" + transactionLogger);
		LOG.debug("Log4JFilePersistor::: Writing data to file with content :::" + transactionListToPersist);

		transactionLogger.info(transactionListToPersist.toString());

		LOG.debug("Log4JFilePersistor::: Successfully wrote data to file with content :::" + transactionListToPersist);
	}

	/**
	 * Creates a logger and attaches file appender to the logger
	 * 
	 * @param fileFolderName File Folder Name
	 * @param fileNameForApp File Name
	 * @param validData
	 * @return {@link Logger}
	 */
	public ch.qos.logback.classic.Logger createRollingLogger(String fileNameForApp) {

		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		LOG.debug("loggerContext :" + loggerContext);

		RollingFileAppender<ILoggingEvent> logFileAppender = new RollingFileAppender<>();
		logFileAppender.setContext(loggerContext);
		logFileAppender.setName(fileNameForApp);
		logFileAppender.setAppend(true);
		logFileAppender.setFile(dataSir + File.separator + fileNameForApp);

		PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
		logEncoder.setContext(loggerContext);
		logEncoder.setPattern(ENCODER_PATTERN);
		logEncoder.start();
		logFileAppender.setEncoder(logEncoder);

		TimeBasedRollingPolicy<ILoggingEvent> logFilePolicy = new TimeBasedRollingPolicy<>();
		logFilePolicy.setContext(loggerContext);
		logFilePolicy.setParent(logFileAppender);
		logFilePolicy.setFileNamePattern(dataSir + File.separator + fileNameForApp + ROLLING_FILE_NAME_PATTERN);
		logFilePolicy.setMaxHistory(30);
		logFilePolicy.setTotalSizeCap(FileSize.valueOf(FILE_SIZE_LIMIT));
		logFilePolicy.start();
		logFileAppender.setRollingPolicy(logFilePolicy);
		logFileAppender.start();

		SizeBasedTriggeringPolicy<ILoggingEvent> sizeTriggerPolicy = new SizeBasedTriggeringPolicy<>();
		sizeTriggerPolicy.setContext(loggerContext);
		sizeTriggerPolicy.setMaxFileSize(FileSize.valueOf(FILE_SIZE_LIMIT));
		logFileAppender.setTriggeringPolicy(sizeTriggerPolicy);
		sizeTriggerPolicy.start();

		ch.qos.logback.classic.Logger transactionLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(fileNameForApp);
		transactionLogger.setAdditive(false);
		transactionLogger.addAppender(logFileAppender);

		StatusPrinter.print(loggerContext);

		LOG.debug("transactionLogger instance created :" + transactionLogger);
		return transactionLogger;
	}

	/**
	 * Returns folder path of the logFileName.
	 * 
	 * @return folder path where strings will be persisted to logFileName.
	 */
	public String getFileFolder() {
		return fileFolder;
	}

	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void rolloutFile() {
		// TODO Auto-generated method stub

	}

	@Override
	public void closeIdleFiles() {
		// TODO Auto-generated method stub

	}

	@Override
	public void purgeStaleLogger() {
		// TODO Auto-generated method stub

	}

}