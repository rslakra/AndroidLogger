package com.rslakra.android.logger;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.widget.Toast;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * See
 * http://developer.android.com/reference/android/util/Log.html#isLoggable(String
 * tag, int level) for the official way to enable and disable logging. This is
 * just a start.
 * <p>
 * This class handles the logs for LogCat as well as file system logs.
 * <p>
 * The logger can also be used as: <code>
 * private static Logger log = AndroidLogger.getLogger(AndroidLogger.class);
 * log.LogHelper("Logging Message Here");
 * </code>
 *
 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
 * @version 1.0.0
 * @created 2015-11-16 06:55:24 PM
 * @since 1.0.0
 */
public final class LogHelper {
    
    /* LOG_TAG */
    public static final String LOG_TAG = "LogHelper".intern();
    
    /* EMPTY_STRING  */
    public static final String EMPTY_STRING = "".intern();
    
    /* LOG_FILE_NAME */
    private final static String LOG_FILE_NAME = "android.log";
    
    /* maximum 3 files */
    private final static int MAX_BACKUP_FILES = 3;
    
    /* 5 MB */
    private final static long MAX_FILE_SIZE = 1024 * 1024 * 5;
    
    /* LOG_PATTERN */
    private final static String LOG_PATTERN = "[%d{yyyy-MM-dd HH:mm:ss.S zzz}] %5p [%c{1}(%L)] - %m%n";
    
    /* mLogType */
    private static LogType sLogType = LogType.INFO;
    
    /* log4JLogsEnabled */
    private static boolean sLog4JLogsEnabled = true;
    
    /* sLog4jConfigurator */
    private final static Log4JConfigurator sLog4jConfigurator = new Log4JConfigurator();
    
    /* mCachedLoggers. */
    private static final Map<String, Logger> mCachedLoggers = new ConcurrentHashMap<String, Logger>();
    
    /**
     * Singleton object
     */
    private LogHelper() {
        throw new UnsupportedOperationException("Object creation not allowed for this class.");
    }
    
    /**
     * Returns true if the android version is greater than 10 (GINGERBREAD_MR1) otherwise false.
     *
     * @return
     */
    public static final boolean isProtectedFileSystem() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1);
    }
    
    /**
     * Returns true if the JVM is android (dalvik) otherwise false.
     *
     * @return
     */
    public static boolean isAndroid() {
        return (System.getProperty("java.vm.name").startsWith("Dalvik".intern()));
    }
    
    /**
     * Returns the system line separator.
     *
     * @return
     */
    public static final String getLineSeparator() {
        return System.getProperty("line.separator");
    }
    
    /**
     * Returns true if the object is null otherwise false.
     *
     * @param object
     * @return
     */
    public static <T> boolean isNull(T object) {
        return (object == null);
    }
    
    /**
     * Returns true if the object is not null otherwise false.
     *
     * @param object
     * @return
     */
    public static <T> boolean isNotNull(T object) {
        return (!isNull(object));
    }
    
    /**
     * Returns true if either the string is null or empty otherwise false.
     *
     * @param string
     * @return
     */
    public static final boolean isNullOrEmpty(final CharSequence string) {
        return (isNull(string) || string.length() == 0);
    }
    
    /**
     * Returns the path combined with the fileName.
     *
     * @param parentFolder
     * @param fileName
     * @return
     */
    public static String pathString(final String parentFolder, final String fileName) {
        if(isNullOrEmpty(parentFolder)) {
            return fileName;
        } else if(isNullOrEmpty(fileName)) {
            return parentFolder;
        } else if(parentFolder.endsWith(fileName)) {
            return parentFolder;
        } else if(fileName.startsWith(parentFolder)) {
            return fileName;
        } else if(parentFolder.endsWith(File.separator) || fileName.startsWith(File.separator)) {
            return parentFolder + fileName;
        } else {
            return parentFolder + File.separator + fileName;
        }
    }
    
    /**
     * Creates the folders/directory, if it does not exist.
     * <p>
     * a/b/c/d/file.log
     *
     * @param mFile
     * @return
     */
    public static boolean makeFolders(final File mFile) {
        if(isNotNull(mFile) && !mFile.exists()) {
            if(!mFile.getParentFile().exists()) {
                makeFolders(mFile.getParentFile());
            }
            
            if(mFile.getParentFile().exists() && !mFile.exists()) {
                if(!mFile.mkdir()) {
                    LogHelper.w(LOG_TAG, "Unable to create [" + mFile.getAbsolutePath() + "] folder.");
                }
            }
            
        }
        
        return true;
    }
    
    /**
     * Deletes the files recursively.
     *
     * @param rootFile
     * @param deleteRecursively
     * @return
     */
    public static boolean deleteRecursively(final File rootFile, final boolean deleteRecursively) {
        if(isNotNull(rootFile) && rootFile.exists()) {
            if(rootFile.isDirectory()) {
                for(File nextFile : rootFile.listFiles()) {
                    if(nextFile.isDirectory() && deleteRecursively) {
                        if(!deleteRecursively(nextFile, deleteRecursively)) {
                            return false;
                        }
                    } else if(nextFile.isFile()) {
                        /* delete root object. */
                        if(!nextFile.delete()) {
                            return false;
                        }
                    }
                }
                
                /* delete root object. */
                if(!rootFile.delete()) {
                    return false;
                }
            } else if(rootFile.isFile()) {
                /* delete root object. */
                if(!rootFile.delete()) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    
    /**
     * Returns the given pathString from assets folder and returns it's input stream.
     *
     * @param pathString
     * @return byte[]
     */
    public static InputStream readAssets(final Context mContext, final String pathString) {
        InputStream assetStream = null;
        if(isNotNull(mContext) && !isNullOrEmpty(pathString)) {
            try {
                assetStream = mContext.getAssets().open(pathString);
            } catch(IOException ex) {
                e(LOG_TAG, ex);
            }
        }
        
        return assetStream;
    }
    
    /**
     * Displays the given message as toast for short period of time.
     *
     * @param context
     * @param message
     */
    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Lock and unlocks the orientation based on the given
     * <code>lockOrientation</code> parameter.
     *
     * @param parentActivity
     * @param lockOrientation
     */
    public static void lockUnlockOrientation(Activity parentActivity, boolean lockOrientation) {
        d(LOG_TAG, "lockUnlockOrientation(" + parentActivity + ", " + lockOrientation + ")");
        if(isNotNull(parentActivity)) {
            if(lockOrientation) {
                if(parentActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    parentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                } else {
                    parentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            } else {
                parentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        }
    }
    
    /**
     * Returns the root folder of the app based on the given <code>mContext</code> context.
     *
     * @param mContext
     * @return
     */
    public static final String getAppRootFolder(final Context mContext) {
        return mContext.getFilesDir().getParent();
    }
    
    /**************************************************************************
     * Log Helper and Logger Configuration Methods.
     **************************************************************************/
    
    /**
     * Returns the sLogType value.
     *
     * @return
     */
    public static LogType getLogType() {
        return sLogType;
    }
    
    /**
     * The sLogType to be set.
     *
     * @param logType
     */
    public static void setLogType(final LogType logType) {
        sLogType = logType;
    }
    
    /**
     * Returns the sLog4JLogsEnabled value.
     *
     * @return
     */
    public static boolean isLog4JLogsEnabled() {
        return sLog4JLogsEnabled;
    }
    
    /**
     * The sLog4JLogsEnabled to be set.
     *
     * @param log4JLogsEnabled
     */
    public static void setLog4JLogsEnabled(final boolean log4JLogsEnabled) {
        sLog4JLogsEnabled = log4JLogsEnabled;
    }
    
    /**
     * Return true if the given <code>logType</code> is same as the default log
     * type otherwise false.
     *
     * @param logType
     * @return
     */
    public static boolean isLogType(final LogType logType) {
        return (getLogType() == logType);
    }
    
    /**
     * Converts the <code>LogType</code> object into <code>Level</code> object.
     *
     * @param logType
     * @return Level
     */
    public static Level toLevel(final LogType logType) {
        switch(logType) {
            case SUPPRESS:
                return Level.OFF;
            case ERROR:
                return Level.ERROR;
            case WARN:
                return Level.WARN;
            case INFO:
                return Level.INFO;
            case DEBUG:
                return Level.DEBUG;
            case VERBOSE:
                return Level.TRACE;
            default:
                throw new RuntimeException("Invalid logType:" + logType);
        }
    }
    
    /**
     * Converts the <code>Level</code> object into <code>LogType</code> object.
     *
     * @param logLevel
     * @return LogType
     */
    public static LogType toLogType(final Level logLevel) {
        if(logLevel == Level.OFF) {
            return LogType.SUPPRESS;
        } else if(logLevel == Level.ERROR) {
            return LogType.ERROR;
        } else if(logLevel == Level.WARN) {
            return LogType.WARN;
        } else if(logLevel == Level.INFO) {
            return LogType.INFO;
        } else if(logLevel == Level.DEBUG) {
            return LogType.DEBUG;
        } else if(logLevel == Level.TRACE) {
            return LogType.VERBOSE;
        } else {
            throw new RuntimeException("Invalid logLevel:" + logLevel);
        }
    }
    
    /**
     * Checks to see whether or not a log for the specified tag is loggable at
     * the specified level.
     * <p>
     * The default level of any tag is set to INFO. This means that any level
     * above and including INFO will be logged. Before you make any calls to a
     * logging method you should check to see if your tag should be logged. You
     * can change the default level by calling
     * <code>setLogType(final LogType logType)</code> method, where level is either
     * VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, or SUPPRESS. SUPPRESS will
     * turn off all logging for your tag. You can also create a local.prop file
     * that with the following in it: 'log.tag.&lt;YOUR_LOG_TAG>=&lt;LEVEL>' and
     * place that in /data/local.prop.
     *
     * @param logType The level to check.
     * @return
     * @throws IllegalArgumentException is thrown if the tag.length() > 23.
     *                                  <p>
     *                                  Returns true if the logging allowed for the
     *                                  <code>logType</code> otherwise false.
     */
    public static boolean isLogEnabledFor(final LogType logType) {
        return (isNull(sLog4jConfigurator) ? false : sLog4jConfigurator.isLogEnabledFor(toLevel(logType)));
    }
    
    /**************************************************************************
     * Configure Log4J logger
     **************************************************************************/
    
    /**
     * Configures the logger with the given settings.
     *
     * @param logFolderPath
     * @param fileName
     * @param logLevel
     * @param logPattern
     * @param maxBackupFiles
     * @param maxFileSize
     */
    public static void log4jConfigure(final String logFolderPath, final String fileName, final LogType logLevel, final String logPattern, final int maxBackupFiles, final long maxFileSize) {
        setLogType(logLevel);
        sLog4jConfigurator.configure(logFolderPath, fileName, Level.toLevel(logLevel.toString()), logPattern, maxBackupFiles, maxFileSize);
    }
    
    /**
     * Configures the logger with the given settings.
     *
     * @param logFolderPath
     * @param fileName
     * @param logLevel
     * @param logPattern
     * @param maxBackupFiles
     */
    public static void log4jConfigure(final String logFolderPath, final String fileName, final LogType logLevel, final String logPattern, final int maxBackupFiles) {
        log4jConfigure(logFolderPath, fileName, logLevel, logPattern, maxBackupFiles, MAX_FILE_SIZE);
    }
    
    /**
     * Configures the logger with the given settings.
     *
     * @param logFolderPath
     * @param fileName
     * @param logLevel
     * @param logPattern
     */
    public static void log4jConfigure(final String logFolderPath, final String fileName, final LogType logLevel, final String logPattern) {
        log4jConfigure(logFolderPath, fileName, logLevel, logPattern, MAX_BACKUP_FILES);
    }
    
    /**
     * Configures the logger with the given settings.
     *
     * @param logFolderPath
     * @param fileName
     * @param logLevel
     */
    public static void log4jConfigure(final String logFolderPath, final String fileName, final LogType logLevel) {
        log4jConfigure(logFolderPath, fileName, logLevel, LOG_PATTERN);
    }
    
    /**
     * Creates the logs in the given logFileName under the parentFolder.
     *
     * @param logFolderPath
     * @param logLevel
     */
    public static void log4jConfigure(final String logFolderPath, final LogType logLevel) {
        log4jConfigure(logFolderPath, LOG_FILE_NAME, logLevel);
    }
    
    /**
     * Creates the logs in the given logFolderPath under the /Android/data folder.
     *
     * @param logFolderPath
     */
    public static void log4jConfigure(final String logFolderPath) {
        log4jConfigure(logFolderPath, LogType.INFO);
    }
    
    /**************************************************************************
     * Log4J Logger methods.
     **************************************************************************/
    
    
    /**
     * Returns the <code>Logger</code> object for the specified
     * <code>logClass</code> class.
     *
     * @param logClass
     * @return
     */
    public static Logger getLogger(Class<?> logClass) {
        Logger logger = null;
        if(isNull(logClass)) {
            throw new IllegalArgumentException("logClass is NULL! it must provide!");
        }
        
        logger = mCachedLoggers.get(logClass.getName());
        if(isNull(logger)) {
            synchronized(mCachedLoggers) {
                if(isNull(logger)) {
                    logger = Logger.getLogger(logClass.getName());
                    /* cache this class logger to reuse */
                    mCachedLoggers.put(logClass.getName(), logger);
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
     * Returns true if the log-level is >= Level.INFO otherwise false.
     *
     * @return
     */
    public static boolean isLogEnabledForProduction() {
        return (sLog4jConfigurator.isLogEnabledFor(Level.INFO));
    }
    
    
    /**************************************************************************
     * Helpers methods.
     **************************************************************************/
    
    /**
     * Returns the formatted string for the given objects.
     *
     * @param format
     * @param objects
     * @return
     */
    private static final String format(final String format, final Object... objects) {
        return String.format(format, objects);
    }
    
    /**
     * Converts the given object into string, if its not null.
     *
     * @param object
     * @return
     */
    public static final String toString(final Object object) {
        return (object == null ? EMPTY_STRING : object.toString());
    }
    
    /**
     * Returns (null) for the null string.
     *
     * @param logMessage
     * @return
     */
    private static final String validateString(final String logMessage) {
        return (logMessage == null ? EMPTY_STRING : logMessage);
    }
    
    /**************************************************************************
     * Log methods.
     **************************************************************************/
    
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
     * Logs WARNING messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void w(final String logTag, final String logMessage) {
        if(isLogEnabledFor(LogType.WARN)) {
            if(isLog4JLogsEnabled()) {
                getLogger(logTag).warn(logMessage);
            } else {
                Log.w(logTag, validateString(logMessage));
            }
        }
    }
    
    /**
     * Logs INFO messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void i(final String logTag, final String logMessage) {
        if(isLogEnabledFor(LogType.INFO)) {
            if(isLog4JLogsEnabled()) {
                getLogger(logTag).info(logMessage);
            } else {
                Log.i(logTag, validateString(logMessage));
            }
        }
    }
    
    /**
     * Logs LogHelper message.
     *
     * @param logTag
     * @param logMessage
     */
    public static void d(final String logTag, final String logMessage) {
        if(isLogEnabledFor(LogType.DEBUG)) {
            if(isLog4JLogsEnabled()) {
                Log.d(logTag, String.valueOf(getLogger(logTag).getLevel()));
                getLogger(logTag).debug(logMessage);
            } else {
                Log.d(logTag, validateString(logMessage));
            }
        }
    }
    
    /**
     * Logs VERBOSE messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void v(final String logTag, final String logMessage) {
        if(isLogEnabledFor(LogType.VERBOSE)) {
            if(isLog4JLogsEnabled()) {
                getLogger(logTag).debug(logMessage);
            } else {
                Log.v(logTag, validateString(logMessage));
            }
        }
    }
    
    /**
     * Logs ERROR messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void e(final String logTag, final String logMessage) {
        if(isLogEnabledFor(LogType.ERROR)) {
            if(isLog4JLogsEnabled()) {
                getLogger(logTag).error(logMessage);
            } else {
                Log.e(logTag, validateString(logMessage));
            }
        }
    }
    
    /**
     * Logs ERROR messages.
     *
     * @param logTag
     * @param throwable
     * @param logMessage
     */
    public static void e(final String logTag, final Throwable throwable, final String logMessage) {
        if(isLogEnabledFor(LogType.ERROR)) {
            if(isLog4JLogsEnabled()) {
                getLogger(logTag).error(logMessage, throwable);
            } else {
                Log.e(logTag, validateString(logMessage), throwable);
            }
        }
    }
    
    /**************************************************************************
     * More-Flexible Helpers methods.
     **************************************************************************/
    
    /**
     * Logs ERROR messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void e(final String logTag, final Object logMessage) {
        e(logTag, toString(logMessage));
    }
    
    /**
     * Logs ERROR messages.
     *
     * @param logTag
     * @param format
     * @param logArguments
     */
    public static void e(final String logTag, final String format, final Object... logArguments) {
        e(logTag, format(format, logArguments));
    }
    
    /**
     * Logs ERROR messages.
     *
     * @param logTag
     * @param throwable
     */
    public static void e(final String logTag, final Throwable throwable) {
        e(logTag, throwable, throwable.getLocalizedMessage());
    }
    
    /**
     * Logs ERROR messages.
     *
     * @param logTag
     * @param throwable
     * @param format
     * @param logArguments
     */
    public static void e(final String logTag, final Throwable throwable, final String format, final Object... logArguments) {
        e(logTag, throwable, format(format, logArguments));
    }
    
    
    /**
     * Logs WARNING messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void w(final String logTag, final Object logMessage) {
        w(logTag, toString(logMessage));
    }
    
    
    /**
     * Logs WARNING messages.
     *
     * @param logTag
     * @param format
     * @param logArguments
     */
    public static void w(final String logTag, final String format, final Object... logArguments) {
        w(logTag, format(format, logArguments));
    }
    
    
    /**
     * Logs INFO messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void i(final String logTag, final Object logMessage) {
        i(logTag, toString(logMessage));
    }
    
    
    /**
     * Logs INFO messages.
     *
     * @param logTag
     * @param format
     * @param logArguments
     */
    public static void i(final String logTag, final String format, final Object... logArguments) {
        i(logTag, format(format, logArguments));
    }
    
    /**
     * Logs LogHelper messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void d(final String logTag, final Object logMessage) {
        d(logTag, toString(logMessage));
    }
    
    /**
     * Logs LogHelper messages.
     *
     * @param logTag
     * @param format
     * @param logArguments
     */
    public static void d(final String logTag, final String format, final Object... logArguments) {
        d(logTag, format(format, logArguments));
    }
    
    /**
     * Logs VERBOSE messages.
     *
     * @param logTag
     * @param logMessage
     */
    public static void v(final String logTag, final Object logMessage) {
        v(logTag, toString(logMessage));
    }
    
    /**
     * Logs VERBOSE messages.
     *
     * @param logTag
     * @param format
     * @param logArguments
     */
    public static void v(final String logTag, final String format, final Object... logArguments) {
        v(logTag, format(format, logArguments));
    }
    
    
    /**
     * What a Terrible Failure: Report an exception that should never happen.
     * Similar to {@link #wtf(String, Throwable)}, with a message as well.
     *
     * @param logTag
     * @param logMessage
     * @param mThrowable
     */
    public static void wtf(final String logTag, final Object logMessage, final Throwable mThrowable) {
        if(isLogEnabledFor(LogType.ERROR)) {
            if(isNull(mThrowable)) {
                Log.wtf(logTag, toString(logMessage));
            } else {
                Log.wtf(logTag, toString(logMessage), mThrowable);
            }
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
    public static void wtf(final String logTag, final Object logMessage) {
        wtf(logTag, logMessage, null);
    }
    
    /**
     * What a Terrible Failure: Report an exception that should never happen.
     *
     * @param logTag
     * @param mThrowable
     */
    public static void wtf(String logTag, final Throwable mThrowable) {
        if(isLogEnabledFor(LogType.ERROR)) {
            Log.wtf(logTag, mThrowable);
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
    
    
    /**************************************************************************
     * Misc Helpers methods.
     **************************************************************************/
    
    /**
     * Logs the URL details.
     *
     * @param logTag
     * @param uri
     */
    @TargetApi(21)
    public static void logUri(final String logTag, final Uri uri) {
        if(isNotNull(uri) && !isNullOrEmpty(logTag)) {
            d(logTag, "urlString:" + uri.toString());
            d(logTag, "Scheme:" + uri.getScheme());
            d(logTag, "Host:" + uri.getHost());
            d(logTag, "QueryParameterNames:" + uri.getQueryParameterNames());
            d(logTag, "Query:" + uri.getQuery());
        }
    }
    
    /**
     * Logs the URL details.
     *
     * @param logTag
     * @param urlString
     */
    public static void logUri(final String logTag, final String urlString) {
        logUri(logTag, Uri.parse(urlString));
    }
    
    /**
     * Logs the <code>WebResourceRequest</code>.
     *
     * @param logTag
     * @param webRequest
     */
    @TargetApi(21)
    public static void logWebRequest(final String logTag, final WebResourceRequest webRequest) {
        if(!isNullOrEmpty(logTag)) {
            d(logTag, "urlString:" + webRequest.getUrl().toString());
            d(logTag, "Method:" + webRequest.getMethod());
            d(logTag, "RequestHeaders:" + webRequest.getRequestHeaders());
            d(logTag, "QueryParameterNames:" + webRequest.getUrl().getQueryParameterNames());
            d(logTag, "Query:" + webRequest.getUrl().getQuery());
        }
    }
    
}