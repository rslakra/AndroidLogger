package com.rslakra.androidlogger;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;

import android.util.Log;

/**
 * An Android log4j Logger.
 * <p>
 * Configures the Log4j logging framework. See <a href=
 * "http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html">Patterns</a>
 * for pattern layout.
 * 
 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
 * @created 2017-07-29 01:34:37 PM
 * @version 1.0.0
 * @since 1.0.0
 */
public final class AndroidLogger {

	/* LOG_FILE_NAME */
	private final static String LOG_FILE_NAME = "dLog4jAndroid.log".intern();

	/* MAX_BACKUP_FILES - maximum 3 files */
	private final static int MAX_BACKUP_FILES = 3;
	/* MAX_FILE_SIZE - 5 MB */
	private final static long MAX_FILE_SIZE = 1024 * 1024 * 5;

	/* DEFAULT_TAG_LAYOUT */
	private static final String DEFAULT_TAG_LAYOUT = "%c";

	/* DEFAULT_LOG_PATTERN */
	public static final String DEFAULT_LOG_PATTERN = "%d - [%p::%c::%C] - %m%n";

	/* ANDROID_LOG_PATTERN */
	public final static String ANDROID_LOG_PATTERN = "[%d{yyyy-MM-dd HH:mm:ss.S zzz}] %5p [%c{1}(%L)] - %m%n";

	/* cachedLoggers. */
	private static final Map<String, Logger> cachedLoggers = new ConcurrentHashMap<String, Logger>();

	/* log4jConfigurator */
	// private final static AndroidLog4JConfigurator log4jConfigurator = new
	// AndroidLog4JConfigurator(null);
	private static AndroidLogger androidLogger;

	/* object properties. */
	private final Logger rootLogger = Logger.getRootLogger();
	private Level logLevel;
	private String filePattern;
	private String logFileName;
	private String mLogFilePath;
	private int maxBackupSize;
	private long maxFileSize;
	private boolean immediateFlush = true;
	private boolean useFileAppender = true;
	private boolean useAndroidAppender = true;
	private boolean resetConfiguration = true;
	private boolean internalDebugging = false;

	/**
	 * Returns true if the <code>object</code> is null otherwise false.
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isNull(Object object) {
		return (object == null);
	}

	/**
	 * Returns true if the <code>charSequence</code> is null or empty otherwise
	 * false.
	 * 
	 * @param charSequence
	 * @return
	 */
	public static boolean isNullOrEmpty(CharSequence charSequence) {
		return (isNull(charSequence) || charSequence.length() == 0);
	}

	/**
	 * This ENUM handles the logTypes supported by <code>Logger</code>.
	 * 
	 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
	 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
	 * @created 2017-08-11 06:55:24 PM
	 * @version 1.0.0
	 * @since 1.0.0
	 */
	public enum LogLevel {
		SUPPRESS, ASSERT, ERROR, WARN, INFO, DEBUG, VERBOSE;
	}

	/* mLogLevel */
	private static LogLevel mLogLevel = LogLevel.INFO;;

	/**
	 * the mLogLevel to be set.
	 * 
	 * @param logLevel
	 */
	public static void setLogLevel(LogLevel logLevel) {
		mLogLevel = logLevel;
	}

	/**
	 * Handy function to get a loggable stack trace from a Throwable.
	 * 
	 * @param mThrowable
	 * @return
	 */
	public static String getStackTraceString(final Throwable mThrowable) {
		return Log.getStackTraceString(mThrowable);
	}

	/**
	 * Checks to see whether or not a log for the specified tag is loggable at
	 * the specified level.
	 *
	 * The default level of any tag is set to INFO. This means that any level
	 * above and including INFO will be logged. Before you make any calls to a
	 * logging method you should check to see if your tag should be logged. You
	 * can change the default level by calling
	 * <code>setLogLevel(LogLevel logLevel)</code> method, where level is either
	 * VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will
	 * turn off all logging for your tag. You can also create a local.prop file
	 * that with the following in it: 'log.tag.&lt;YOUR_LOG_TAG>=&lt;LEVEL>' and
	 * place that in /data/local.prop.
	 *
	 * @param tag
	 *            The tag to check.
	 * @param level
	 *            The level to check.
	 * @return Whether or not that this is allowed to be logged.
	 * @throws IllegalArgumentException
	 *             is thrown if the tag.length() > 23.
	 */
	public static boolean isLoggable(LogLevel logLevel) {
		return (logLevel != null && logLevel.ordinal() >= mLogLevel.ordinal());
	}

	/**
	 * Send a VERBOSE log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @return
	 */
	public static void v(final String logTag, final String logMessage) {
		if (isLoggable(LogLevel.VERBOSE)) {
			Log.v(logTag, logMessage);
		}
	}

	/**
	 * Send a VERBOSE log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @param mThrowable
	 * @return
	 */
	public static void v(final String logTag, final String logMessage, final Throwable mThrowable) {
		if (isLoggable(LogLevel.VERBOSE)) {
			Log.v(logTag, logMessage, mThrowable);
		}
	}

	/**
	 * Send a DEBUG log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @return
	 */
	public static void d(final String logTag, final String logMessage) {
		if (isLoggable(LogLevel.DEBUG)) {
			Log.d(logTag, logMessage);
		}
	}

	/**
	 * Send a DEBUG log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @param mThrowable
	 * @return
	 */
	public static void d(final String logTag, final String logMessage, final Throwable mThrowable) {
		if (isLoggable(LogLevel.DEBUG)) {
			Log.d(logTag, logMessage, mThrowable);
		}
	}

	/**
	 * Send a INFO log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @return
	 */
	public static void i(final String logTag, final String logMessage) {
		if (isLoggable(LogLevel.INFO)) {
			Log.i(logTag, logMessage);
		}
	}

	/**
	 * Send a INFO log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @param mThrowable
	 * @return
	 */
	public static void i(final String logTag, final String logMessage, final Throwable mThrowable) {
		if (isLoggable(LogLevel.INFO)) {
			Log.i(logTag, logMessage, mThrowable);
		}
	}

	/**
	 * Send a WARN log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @return
	 */
	public static void w(final String logTag, final String logMessage) {
		if (isLoggable(LogLevel.WARN)) {
			Log.w(logTag, logMessage);
		}
	}

	/**
	 * Send a WARN log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @param mThrowable
	 * @return
	 */
	public static void w(final String logTag, final String logMessage, final Throwable mThrowable) {
		if (isLoggable(LogLevel.WARN)) {
			Log.w(logTag, logMessage, mThrowable);
		}
	}

	/**
	 * Send a ERROR log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @return
	 */
	public static void e(final String logTag, final String logMessage) {
		if (isLoggable(LogLevel.ERROR)) {
			Log.e(logTag, logMessage);
		}
	}

	/**
	 * Send a ERROR log message.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @param mThrowable
	 * @return
	 */
	public static void e(final String logTag, final String logMessage, final Throwable mThrowable) {
		if (isLoggable(LogLevel.ERROR)) {
			Log.e(logTag, logMessage, mThrowable);
		}
	}

	/**
	 * What a Terrible Failure: Report a condition that should never happen. The
	 * error will always be logged at level ASSERT with the call stack.
	 * Depending on system configuration, a report may be added to the
	 * {@link android.os.DropBoxManager} and/or the process may be terminated
	 * immediately with an error dialog.
	 * 
	 * @param logTag
	 * @param logMessage
	 */
	public static void wtf(final String logTag, final String logMessage) {
		if (isLoggable(LogLevel.ASSERT)) {
			Log.wtf(logTag, logMessage);
		}
	}

	/**
	 * What a Terrible Failure: Report an exception that should never happen.
	 * Similar to {@link #wtf(String, String)}, with an exception to log.
	 * 
	 * @param logTag
	 * @param mThrowable
	 */
	public static void wtf(String logTag, final Throwable mThrowable) {
		if (isLoggable(LogLevel.ASSERT)) {
			Log.wtf(logTag, mThrowable);
		}
	}

	/**
	 * What a Terrible Failure: Report an exception that should never happen.
	 * Similar to {@link #wtf(String, Throwable)}, with a message as well.
	 * 
	 * @param logTag
	 * @param logMessage
	 * @param mThrowable
	 */
	public static void wtf(final String logTag, final String logMessage, final Throwable mThrowable) {
		if (isLoggable(LogLevel.ASSERT)) {
			Log.wtf(logTag, logMessage, mThrowable);
		}
	}

	/**
	 * Low-level logging call.
	 * 
	 * @param logPriority
	 * @param logTag
	 * @param logMessage
	 * @return
	 */
	public static int println(int logPriority, final String logTag, final String logMessage) {
		return Log.println(logPriority, logTag, logMessage);
	}

	/**
	 * Configures the logger with the privided settings.
	 * 
	 * @param logFileName
	 * @param logLevel
	 * @param filePattern
	 * @param maxBackupFiles
	 * @param maxFileSize
	 */
	public static void log4jConfigure(String logFileName, Level logLevel, String filePattern, int maxBackupFiles,
			long maxFileSize) {
		if (isNull(logLevel)) {
			logLevel = Level.WARN;
		}

		/* create only one instance of this logger. */
		if (isNull(androidLogger)) {
			synchronized (AndroidLogger.class) {
				if (isNull(androidLogger)) {
					androidLogger = new AndroidLogger(logFileName, logLevel, filePattern, maxBackupFiles, maxFileSize);
				}
			}
		}

		// configure
		androidLogger.configure();
	}

	/**
	 * Configures the logger with the given settings.
	 * 
	 * @param fileName
	 * @param filePattern
	 * @param maxBackupFiles
	 * @param maxFileSize
	 */
	public static void log4jConfigure(String logFileName, Level logLevel, String filePattern, int maxBackupFiles) {
		log4jConfigure(logFileName, logLevel, filePattern, maxBackupFiles, MAX_FILE_SIZE);
	}

	/**
	 * Configures the logger with the given settings.
	 * 
	 * @param fileName
	 * @param filePattern
	 * @param maxBackupFiles
	 * @param maxFileSize
	 */
	public static void log4jConfigure(String logFileName, Level logLevel, String filePattern) {
		log4jConfigure(logFileName, logLevel, filePattern, MAX_BACKUP_FILES);
	}

	/**
	 * Creates the logs in the given logFileName under the parentFolder.
	 * 
	 * @param parentFolder
	 * @param logFileName
	 */
	public static void log4jConfigure(String logFileName, Level logLevel) {
		log4jConfigure(logFileName, logLevel, ANDROID_LOG_PATTERN, MAX_BACKUP_FILES, MAX_FILE_SIZE);
	}

	/**
	 * Creates the logs in the given logFileName under the /Android/data folder.
	 * 
	 * @param logFileName
	 */
	public static void log4jConfigure(String logFileName) {
		log4jConfigure(logFileName);
	}

	/**
	 * Creates the logs in the given logFileName under the /Android/data folder.
	 * 
	 * @param logFileName
	 */
	public static void log4jConfigure() {
		log4jConfigure(LOG_FILE_NAME);
	}

	/**
	 * Returns the <code>Logger</code> object for the specified
	 * <code>Class<?></code> class.
	 * 
	 * @param klass
	 * @return
	 */
	public static Logger getLogger(Class<?> klass) {
		Logger logger = null;
		if (isNull(klass)) {
			throw new IllegalArgumentException("klass name should not be null.");
		}

		logger = cachedLoggers.get(klass.getName());
		if (isNull(logger)) {
			synchronized (LogManager.class) {
				if (isNull(logger)) {
					logger = Logger.getLogger(klass.getName());
					/* cache this class logger to reuse */
					cachedLoggers.put(klass.getName(), logger);
				}
			}
		}

		return logger;
	}

	/**
	 * Returns the logger for this given className.
	 * 
	 * @param className
	 * @return
	 */
	public static Logger getLogger(String className) {
		return Logger.getLogger(className);
	}

	/**
	 * 
	 * @param logFileName
	 * @param rootLevel
	 * @param filePattern
	 * @param maxBackupSize
	 * @param maxFileSize
	 */
	public AndroidLogger(final String logFileName, final Level logLevel, final String filePattern,
			final int maxBackupSize, final long maxFileSize) {
		// set the logger level
		setLogLevel(logLevel);
		// set the name of the log file
		setLogFileName(logFileName);
		// set output format of the log line
		setFilePattern(filePattern);
		// Maximum number of backed up log files
		setMaxBackupSize(maxBackupSize);
		// Maximum size of log file until rolling
		setMaxFileSize(maxFileSize);
	}

	/**
	 * 
	 * @param logFileName
	 * @param logLevel
	 * @param filePattern
	 */
	public AndroidLogger(final String logFileName, final Level logLevel, final String filePattern) {
		this(logFileName, logLevel, filePattern, MAX_BACKUP_FILES, MAX_FILE_SIZE);
	}

	/**
	 * 
	 * @param logFileName
	 * @param logLevel
	 */
	public AndroidLogger(final String logFileName, final Level logLevel) {
		this(logFileName, logLevel, DEFAULT_LOG_PATTERN);
	}

	/**
	 * @param logFileName
	 */
	public AndroidLogger(final String logFileName) {
		this(logFileName, Level.WARN);
	}

	/**
	 * Sets the level of logger with name <code>loggerName</code>. Corresponds
	 * to log4j.properties <code>log4j.logger.org.apache.what.ever=ERROR</code>
	 *
	 * @param loggerName
	 * @param logLevel
	 */
	public void setLevel(final String loggerName, final Level logLevel) {
		Logger.getLogger(loggerName).setLevel(logLevel);
	}

	/**
	 * Return the log level of the root logger
	 *
	 * @return
	 */
	public Level getLogLevel() {
		return logLevel;
	}

	/**
	 * The rootLevel logger to be set.
	 *
	 * @param level
	 */
	public void setLogLevel(final Level logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * Returns the filePattern.
	 * 
	 * @return
	 */
	public String getFilePattern() {
		return filePattern;
	}

	/**
	 * The filePattern to be set.
	 * 
	 * @param filePattern
	 */
	public void setFilePattern(final String filePattern) {
		this.filePattern = filePattern;
	}

	/**
	 * Returns the name of the log file.
	 *
	 * @return
	 */
	public String getLogFileName() {
		return logFileName;
	}

	/**
	 * The logFileName to be set.
	 *
	 * @param logFileName
	 */
	public void setLogFileName(final String logFileName) {
		this.logFileName = logFileName;
	}

	/**
	 * Returns the maximum number of backed up log files
	 *
	 * @return
	 */
	public int getMaxBackupSize() {
		return maxBackupSize;
	}

	/**
	 * Sets the maximum number of backed up log files
	 *
	 * @param maxBackupSize
	 */
	public void setMaxBackupSize(final int maxBackupSize) {
		this.maxBackupSize = maxBackupSize;
	}

	/**
	 * Returns the maximum size of log file until rolling
	 *
	 * @return
	 */
	public long getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * Sets the maximum size of log file until rolling
	 *
	 * @param maxFileSize
	 */
	public void setMaxFileSize(final long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	/**
	 * Returns the immediateFlush.
	 *
	 * @return
	 */
	public boolean isImmediateFlush() {
		return immediateFlush;
	}

	/**
	 * The immediateFlush to be set.
	 *
	 * @param immediateFlush
	 */
	public void setImmediateFlush(final boolean immediateFlush) {
		this.immediateFlush = immediateFlush;
	}

	/**
	 * Returns true, if FileAppender is used for logging
	 *
	 * @return
	 */
	public boolean isUseFileAppender() {
		return useFileAppender;
	}

	/**
	 * The useFileAppender to be set.
	 * 
	 * @param useFileAppender
	 */
	public void setUseFileAppender(final boolean useFileAppender) {
		this.useFileAppender = useFileAppender;
	}

	/**
	 * Returns true, if LogcatAppender should be used
	 *
	 * @return
	 */
	public boolean isUseAndroidAppender() {
		return useAndroidAppender;
	}

	/**
	 * The useAndroidAppender to be set. If true, LogCatAppender will be used
	 * for logging
	 * 
	 * @param useAndroidAppender
	 */
	public void setUseAndroidAppender(final boolean useAndroidAppender) {
		this.useAndroidAppender = useAndroidAppender;
	}

	/**
	 * Resets the log4j configuration before applying this configuration.
	 * Default is true.
	 *
	 * @return True, if the log4j configuration should be reset before applying
	 *         this configuration.
	 */
	public boolean isResetConfiguration() {
		return resetConfiguration;
	}

	/**
	 * The resetConfiguration to be set.
	 * 
	 * @param resetConfiguration
	 */
	public void setResetConfiguration(boolean resetConfiguration) {
		this.resetConfiguration = resetConfiguration;
	}

	/**
	 * Returns the InternalDebugging.
	 *
	 * @return
	 */
	public boolean isInternalDebugging() {
		return internalDebugging;
	}

	/**
	 * The InternalDebugging to be set.
	 *
	 * @param internalDebugging
	 */
	public void setInternalDebugging(boolean internalDebugging) {
		this.internalDebugging = internalDebugging;
	}

	/**
	 * Returns the sLogFilePath.
	 * 
	 * @return
	 */
	public String getLogFilePath() {
		return mLogFilePath;
	}

	/**
	 * The logFilePath to be set. If the <code>parentFolder</code> is null or
	 * empty, the user's directory will be the parent's folder.
	 * 
	 * @param parentFolder
	 * @param logFileName
	 */
	public void setLogFilePath(String parentFolder, String logFileName) {
		if (isNullOrEmpty(parentFolder)) {
			// throw new InvalidParameterException("parentFolder should not be
			// null or empty!");
			parentFolder = System.getProperty("user.dir");
		}

		if (isNullOrEmpty(logFileName)) {
			throw new IllegalArgumentException("filePath should not be null or empty!");
		}

		if (parentFolder.endsWith(File.separator) && logFileName.startsWith(File.separator)) {
			setLogFilePath(parentFolder + logFileName.substring(1));
		} else if (parentFolder.endsWith(File.separator) && !logFileName.startsWith(File.separator)) {
			setLogFilePath(parentFolder + logFileName);
		} else if (!parentFolder.endsWith(File.separator) && logFileName.startsWith(File.separator)) {
			setLogFilePath(parentFolder + logFileName);
		} else {
			setLogFilePath(parentFolder + File.separator + logFileName);
		}
	}

	/**
	 * The mLogFilePath to be set.
	 * 
	 * @param logFilePath
	 */
	public void setLogFilePath(String logFilePath) {
		mLogFilePath = logFilePath;
	}

	/**
	 * Configures the logger for file appender and android.
	 */
	public void configure() {
		if (isResetConfiguration()) {
			LogManager.getLoggerRepository().resetConfiguration();
		}

		LogLog.setInternalDebugging(isInternalDebugging());

		if (isUseFileAppender()) {
			/* Configures the file appender. */
			final RollingFileAppender rollingFileAppender;
			try {
				rollingFileAppender = new RollingFileAppender(new PatternLayout(getFilePattern()), getLogFileName());
				rollingFileAppender.setMaxBackupIndex(getMaxBackupSize());
				rollingFileAppender.setMaximumFileSize(getMaxFileSize());
				rollingFileAppender.setImmediateFlush(isImmediateFlush());
			} catch (Exception ex) {
				throw new RuntimeException("Exception configuring system logger!", ex);
			}

			rootLogger.addAppender(rollingFileAppender);
		}

		if (isUseAndroidAppender()) {
			/* Configures an android appender. */
			rootLogger.addAppender(
					new AndroidAppender(new PatternLayout(DEFAULT_TAG_LAYOUT), new PatternLayout(getFilePattern())));
		}

		rootLogger.setLevel(getLogLevel());
	}
}
