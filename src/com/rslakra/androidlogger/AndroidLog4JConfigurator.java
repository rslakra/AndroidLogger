///******************************************************************************
// * Copyright (C) Devamatre Technologies 2017
// * 
// * This code is licensed to Devamatre under one or more contributor license 
// * agreements. The reproduction, transmission or use of this code or the 
// * snippet is not permitted without prior express written consent of Devamatre. 
// * 
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the license is distributed on an "AS IS" BASIS, WITHOUT 
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied and the 
// * offenders will be liable for any damages. All rights, including  but not
// * limited to rights created by patent grant or registration of a utility model 
// * or design, are reserved. Technical specifications and features are binding 
// * only insofar as they are specifically and expressly agreed upon in a written 
// * contract.
// * 
// * You may obtain a copy of the License for more details at:
// *      http://www.devamatre.com/licenses/license.txt.
// *      
// * Devamatre reserves the right to modify the technical specifications and or 
// * features without any prior notice.
// *****************************************************************************/
//package com.rslakra.androidlogger;
//
//import org.apache.log4j.Layout;
//import org.apache.log4j.Level;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PatternLayout;
//import org.apache.log4j.RollingFileAppender;
//import org.apache.log4j.helpers.LogLog;
//
///**
// * An Android log4j configurator.
// * <p>
// * Configures the Log4j logging framework. See <a href=
// * "http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html">Patterns</a>
// * for pattern layout.
// * 
// * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
// * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
// * @created 2017-07-29 02:08:43 PM
// * @version 1.0.0
// * @since 1.0.0
// */
//public final class AndroidLog4JConfigurator {
//
//	public static final String DEFAULT_FILE_NAME = "log4jAndroid.log";
//	public static final String DEFAULT_PATTERN = "%d - [%p::%c::%C] - %m%n";
//	/* maximum 3 files */
//	public final static int MAX_BACKUP_FILES = 3;
//	/* 5 MB */
//	public final static long MAX_FILE_SIZE = 1024 * 1024 * 5;
//
//	private Level rootLevel = Level.DEBUG;
//	private String filePattern = DEFAULT_PATTERN;
//	private String logFileName = DEFAULT_FILE_NAME;
//	private int maxBackupSize = MAX_BACKUP_FILES;
//	private long maxFileSize = MAX_FILE_SIZE;
//	private boolean immediateFlush = true;
//	private boolean useFileAppender = true;
//	private boolean useAndroidAppender = true;
//	private boolean resetConfiguration = true;
//	private boolean internalDebugging = false;
//
//	/**
//	 * @param logFileName
//	 */
//	public AndroidLog4JConfigurator(final String logFileName) {
//		setLogFileName(logFileName);
//	}
//
//	/**
//	 * @param logFileName
//	 * @param rootLevel
//	 */
//	public AndroidLog4JConfigurator(final String logFileName, final Level rootLevel) {
//		this(logFileName);
//		setRootLevel(rootLevel);
//	}
//
//	/**
//	 * @param fileName
//	 *            Name of the log file
//	 * @param rootLevel
//	 *            Log level for the root logger
//	 * @param filePattern
//	 *            Log pattern for the file appender
//	 */
//	public AndroidLog4JConfigurator(final String logFileName, final Level rootLevel, final String filePattern) {
//		this(logFileName, rootLevel);
//		setFilePattern(filePattern);
//	}
//
//	/**
//	 * @param fileName
//	 *            Name of the log file
//	 * @param maxBackupSize
//	 *            Maximum number of backed up log files
//	 * @param maxFileSize
//	 *            Maximum size of log file until rolling
//	 * @param filePattern
//	 *            Log pattern for the file appender
//	 * @param rootLevel
//	 *            Log level for the root logger
//	 */
//	public AndroidLog4JConfigurator(final String fileName, final int maxBackupSize, final long maxFileSize,
//			final String filePattern, final Level rootLevel) {
//		this(fileName, rootLevel, filePattern);
//		setMaxBackupSize(maxBackupSize);
//		setMaxFileSize(maxFileSize);
//	}
//
//	/**
//	 * Configures the logger for file appender and android.
//	 */
//	public void configure() {
//		final Logger rootLogger = Logger.getRootLogger();
//
//		if (isResetConfiguration()) {
//			LogManager.getLoggerRepository().resetConfiguration();
//		}
//
//		LogLog.setInternalDebugging(isInternalDebugging());
//
//		if (isUseFileAppender()) {
//			configureFileAppender();
//		}
//
//		if (isUseAndroidAppender()) {
//			configureAndroidAppender();
//		}
//
//		rootLogger.setLevel(getRootLevel());
//	}
//
//	/**
//	 * Sets the level of logger with name <code>loggerName</code>. Corresponds
//	 * to log4j.properties <code>log4j.logger.org.apache.what.ever=ERROR</code>
//	 *
//	 * @param loggerName
//	 * @param level
//	 */
//	public void setLevel(final String loggerName, final Level level) {
//		Logger.getLogger(loggerName).setLevel(level);
//	}
//
//	/**
//	 * Configures the file appender.
//	 */
//	private void configureFileAppender() {
//		final Logger root = Logger.getRootLogger();
//		final Layout patternLayout = new PatternLayout(getFilePattern());
//		final RollingFileAppender rollingFileAppender;
//
//		try {
//			rollingFileAppender = new RollingFileAppender(patternLayout, getLogFileName());
//		} catch (Exception ex) {
//			throw new RuntimeException("Exception configuring system logger!", ex);
//		}
//
//		rollingFileAppender.setMaxBackupIndex(getMaxBackupSize());
//		rollingFileAppender.setMaximumFileSize(getMaxFileSize());
//		rollingFileAppender.setImmediateFlush(isImmediateFlush());
//
//		root.addAppender(rollingFileAppender);
//	}
//
//	/**
//	 * Configures an android appender.
//	 */
//	private void configureAndroidAppender() {
//		final Logger root = Logger.getRootLogger();
//		final Layout patternLayout = new PatternLayout(getFilePattern());
//		final AndroidAppender androidAppender = new AndroidAppender(patternLayout);
//		root.addAppender(androidAppender);
//	}
//
//	/**
//	 * Return the log level of the root logger
//	 *
//	 * @return
//	 */
//	public Level getRootLevel() {
//		return rootLevel;
//	}
//
//	/**
//	 * Sets log level for the root logger.
//	 *
//	 * @param level
//	 */
//	public void setRootLevel(final Level level) {
//		this.rootLevel = level;
//	}
//
//	/**
//	 * @return
//	 */
//	public String getFilePattern() {
//		return filePattern;
//	}
//
//	/**
//	 * @param filePattern
//	 */
//	public void setFilePattern(final String filePattern) {
//		this.filePattern = filePattern;
//	}
//
//	/**
//	 * Returns the name of the log file.
//	 *
//	 * @return
//	 */
//	public String getLogFileName() {
//		return logFileName;
//	}
//
//	/**
//	 * The logFileName to be set.
//	 *
//	 * @param logFileName
//	 */
//	public void setLogFileName(final String logFileName) {
//		this.logFileName = logFileName;
//	}
//
//	/**
//	 * Returns the maximum number of backed up log files
//	 *
//	 * @return
//	 */
//	public int getMaxBackupSize() {
//		return maxBackupSize;
//	}
//
//	/**
//	 * Sets the maximum number of backed up log files
//	 *
//	 * @param maxBackupSize
//	 */
//	public void setMaxBackupSize(final int maxBackupSize) {
//		this.maxBackupSize = maxBackupSize;
//	}
//
//	/**
//	 * Returns the maximum size of log file until rolling
//	 *
//	 * @return
//	 */
//	public long getMaxFileSize() {
//		return maxFileSize;
//	}
//
//	/**
//	 * Sets the maximum size of log file until rolling
//	 *
//	 * @param maxFileSize
//	 */
//	public void setMaxFileSize(final long maxFileSize) {
//		this.maxFileSize = maxFileSize;
//	}
//
//	/**
//	 * Returns the immediateFlush.
//	 *
//	 * @return
//	 */
//	public boolean isImmediateFlush() {
//		return immediateFlush;
//	}
//
//	/**
//	 * The immediateFlush to be set.
//	 *
//	 * @param immediateFlush
//	 */
//	public void setImmediateFlush(final boolean immediateFlush) {
//		this.immediateFlush = immediateFlush;
//	}
//
//	/**
//	 * Returns true, if FileAppender is used for logging
//	 *
//	 * @return
//	 */
//	public boolean isUseFileAppender() {
//		return useFileAppender;
//	}
//
//	/**
//	 * The useFileAppender to be set.
//	 * 
//	 * @param useFileAppender
//	 */
//	public void setUseFileAppender(final boolean useFileAppender) {
//		this.useFileAppender = useFileAppender;
//	}
//
//	/**
//	 * Returns true, if LogcatAppender should be used
//	 *
//	 * @return
//	 */
//	public boolean isUseAndroidAppender() {
//		return useAndroidAppender;
//	}
//
//	/**
//	 * The useAndroidAppender to be set. If true, LogCatAppender will be used
//	 * for logging
//	 * 
//	 * @param useAndroidAppender
//	 */
//	public void setUseAndroidAppender(final boolean useAndroidAppender) {
//		this.useAndroidAppender = useAndroidAppender;
//	}
//
//	/**
//	 * Resets the log4j configuration before applying this configuration.
//	 * Default is true.
//	 *
//	 * @return True, if the log4j configuration should be reset before applying
//	 *         this configuration.
//	 */
//	public boolean isResetConfiguration() {
//		return resetConfiguration;
//	}
//
//	/**
//	 * The resetConfiguration to be set.
//	 * 
//	 * @param resetConfiguration
//	 */
//	public void setResetConfiguration(boolean resetConfiguration) {
//		this.resetConfiguration = resetConfiguration;
//	}
//
//	/**
//	 * Returns the InternalDebugging.
//	 *
//	 * @return
//	 */
//	public boolean isInternalDebugging() {
//		return internalDebugging;
//	}
//
//	/**
//	 * The InternalDebugging to be set.
//	 *
//	 * @param internalDebugging
//	 */
//	public void setInternalDebugging(boolean internalDebugging) {
//		this.internalDebugging = internalDebugging;
//	}
//}
