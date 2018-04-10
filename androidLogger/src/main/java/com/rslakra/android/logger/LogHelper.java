/******************************************************************************
 * Copyright (C) Devamatre Inc. 2009-2018. All rights reserved.
 *
 * This code is licensed to Devamatre under one or more contributor license
 * agreements. The reproduction, transmission or use of this code, in source
 * and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 * 	  notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 * Devamatre reserves the right to modify the technical specifications and or
 * features without any prior notice.
 *****************************************************************************/
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

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
    
    /* SPACE  */
    public static final String SPACE = " ".intern();
    
    /* ANDROID_LOG4J_PROPERTIES  */
    public static final String ANDROID_LOG4J_PROPERTIES = "android_log4j.properties".intern();
    
    /* ANDROID_LOG4J_XML  */
    private static final String ANDROID_LOG4J_XML = "android_log4j.xml".intern();
    
    /* LOG4J_ROOT_LOGGER */
    private final static String LOG4J_ROOT_LOGGER = "log4j.rootLogger";
    
    /* CONSOLE_LOG_PATTERN */
    private final static String CONSOLE_LOG_PATTERN = "log4j.appender.console.layout.ConversionPattern";
    
    /* KEY_RFA_FILE */
    private final static String KEY_RFA_FILE = "log4j.appender.RFA.File";
    
    /* KEY_RFA_MAX_FILE_SIZE */
    private final static String KEY_RFA_MAX_FILE_SIZE = "log4j.appender.RFA.MaxFileSize";
    
    /* KEY_RFA_MAX_BACKUP_FILES */
    private final static String KEY_RFA_MAX_BACKUP_FILES = "log4j.appender.RFA.MaxBackupIndex";
    
    /* KEY_RFA_LOG_PATTERN */
    private final static String KEY_RFA_LOG_PATTERN = "log4j.appender.RFA.layout.ConversionPattern";
    
    /* LOG_FILE_NAME */
    public final static String LOG_FILE_NAME = "android.log";
    
    /* maximum 3 files */
    public final static int MAX_BACKUP_FILES = 2;
    
    /* MB_SIZE */
    public final static long MB_SIZE = 1024 * 1024;
    
    /* 2 MB */
    public final static long MAX_FILE_SIZE = MB_SIZE * 2;
    
    /* LOG_PATTERN */
    public final static String LOG_PATTERN = "[%d{yyyy-MM-dd HH:mm:ss.S}] %5p [%t] [%c{1}(%L)] - %m%n";
    
    /* mLogType */
    private static LogType sLogType = LogType.INFO;
    
    /* log4JLogsEnabled */
    private static boolean sLog4JLogsEnabled = false;
    
    /* sLog4JConfigurator */
    private final static Log4JConfigurator sLog4JConfigurator = new Log4JConfigurator();
    
    /**
     * Singleton object
     */
    private LogHelper() {
        throw new UnsupportedOperationException("Object creation is not allowed for this class!");
    }
    
    /**************************************************************************
     * Helper Methods.
     **************************************************************************/
    
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
     * Returns the value of the <code>line.separator</code> system property.
     *
     * @return
     */
    public static final String getLineSeparator() {
        return System.getProperty("line.separator");
    }
    
    /**
     * Returns the value of the <code>user.home</code> system property.
     *
     * @return
     */
    public static final String getUserHome() {
        return System.getProperty("user.home");
    }
    
    
    /**
     * Returns the value of the <code>user.dir</code> system property.
     *
     * @return
     */
    public static final String getUserDir() {
        return System.getProperty("user.dir");
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
     * Returns the string representation of the specified <code>strings</code> array. If the
     * specified <code>withNewLine</code> flag is set to be true, the new line character is added at
     * the beginning and after each string in the array.
     *
     * @param strings
     * @return
     */
    public static String toString(final String[] strings, final boolean withNewLine) {
        final StringBuilder sBuilder = new StringBuilder();
        if(strings != null) {
            if(withNewLine) {
                sBuilder.append("\n");
            }
            
            for(String string : strings) {
                sBuilder.append(string);
                if(withNewLine) {
                    sBuilder.append("\n");
                } else {
                    sBuilder.append(SPACE);
                }
            }
        }
        
        return sBuilder.toString();
    }
    
    /**
     * Returns the string representation of the specified <code>strings</code> array.
     *
     * @param strings
     * @return
     */
    public static String toString(final String[] strings) {
        return toString(strings, false);
    }
    
    /**
     * Returns the string representation of the specified <code>object</code> object, if it's not
     * <code>null</code> otherwise empty string.
     *
     * @param object
     * @return
     */
    public static final String toString(final Object object) {
        return (isNull(object) ? EMPTY_STRING : object.toString());
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
                if(!mFile.mkdirs()) {
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
     * Returns the root folder of the app based on the given <code>mContext</code> context.
     *
     * @param mContext
     * @return
     */
    public static final String getAppRootFolder(final Context mContext) {
        return mContext.getFilesDir().getParent();
    }
    
    /**
     * Returns the <code>InputStream</code> from the assets folders for the given
     * <code>pathString</code> asset.
     *
     * @param context
     * @param pathString
     * @return
     */
    public static InputStream readAssets(final Context context, final String pathString) {
        InputStream assetStream = null;
        if(isNotNull(context) && !isNullOrEmpty(pathString)) {
            try {
                assetStream = context.getAssets().open(pathString);
            } catch(IOException ex) {
                e(LOG_TAG, ex);
            }
        }
        
        return assetStream;
    }
    
    /**
     * Returns the <code>InputStream</code> from the <code>../res/raw</code> folders for the given
     * <code>fileName</code> resource.
     * <p>
     * Returns the given pathString from assets folder and returns it's input stream.
     *
     * @param context
     * @param fileName
     * @return
     */
    public static InputStream readRAWResources(final Context context, final String fileName) {
        InputStream rawResourcesStream = null;
        if(isNotNull(context) && !isNullOrEmpty(fileName)) {
            rawResourcesStream = context.getResources().openRawResource(context.getResources().getIdentifier(fileName, "raw", context.getPackageName()));
        }
        
        return rawResourcesStream;
    }
    
    /**
     * Displays the given <code>message</code> as toast for the given <code>duration</code> or
     * period of time.
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void showToastMessage(final Context context, final String message, final int duration) {
        Toast.makeText(context, message, duration).show();
    }
    
    /**
     * Displays the given <code>message</code> as toast for the short period of time.
     *
     * @param context
     * @param message
     */
    public static void showToastMessage(final Context context, final String message) {
        showToastMessage(context, message, Toast.LENGTH_SHORT);
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
     * Loads the <code>Properties</code> object for the specified <code>inputStream</code>.
     *
     * @param inputStream
     * @return
     */
    public static final Properties loadProperties(final InputStream inputStream) {
        final Properties mProperties = new Properties();
        try {
            mProperties.load(inputStream);
        } catch(Exception ex) {
            Log.e(LOG_TAG, "Error loading properties file from the stream!", ex);
        }
        
        return mProperties;
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
        return (isLog4JLogsEnabled() ? LogType.toLogType(sLog4JConfigurator.getLogLevel()) : sLogType);
    }
    
    /**
     * The sLogType to be set.
     *
     * @param logType
     */
    public static final void setLogType(final LogType logType) {
        if(isLog4JLogsEnabled()) {
            sLog4JConfigurator.setLogLevel(LogType.toLevel(logType));
        }
        sLogType = logType;
    }
    
    /**
     * Return true if the given <code>logType</code> is same as the default log
     * type otherwise false.
     *
     * @param logType
     * @return
     */
    public static final boolean isLogType(final LogType logType) {
        return (getLogType() == logType);
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
        if(sLog4JLogsEnabled != log4JLogsEnabled) {
            sLog4JLogsEnabled = log4JLogsEnabled;
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
        if(isLog4JLogsEnabled()) {
            return (sLog4JConfigurator.isLogEnabledFor(LogType.toLevel(logType)));
        } else {
            return (isNotNull(logType) && logType.ordinal() >= getLogType().ordinal());
        }
    }
    
    /**************************************************************************
     * Configure Log4J logger
     **************************************************************************/
    
    /**
     * The Log4J logger is configured based on the specified configurations.
     *
     * @param logFolderPath
     * @param fileName
     * @param logLevel
     * @param logPattern
     * @param maxBackupFiles
     * @param maxFileSize
     */
    public static void log4JConfigure(final String logFolderPath, final String fileName, final LogType logLevel, final String logPattern, final int maxBackupFiles, final long maxFileSize) {
        setLog4JLogsEnabled(true);
        /* setting all the properties in the reverse order. */
        /** the logs folder to be set. */
        sLog4JConfigurator.setLogsFolder(logFolderPath);
        
        /** the log file name to be set. */
        sLog4JConfigurator.setFileName(fileName);
        
        /** the root log level to be set. */
        sLog4JConfigurator.setLogLevel(LogType.toLevel(logLevel));
        
        /** the logs pattern to be set. */
        sLog4JConfigurator.setLogPattern(logPattern);
        
        /** the maximum number of backup files to be created. */
        sLog4JConfigurator.setMaxBackupFiles(maxBackupFiles);
        
        /** the maximum log file size to be set. */
        sLog4JConfigurator.setMaxFileSize(maxFileSize);
        
        //configure the log4j logger
        sLog4JConfigurator.configure();
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
    public static void log4JConfigure(final String logFolderPath, final String fileName, final LogType logLevel, final String logPattern, final int maxBackupFiles) {
        log4JConfigure(logFolderPath, fileName, logLevel, logPattern, maxBackupFiles, MAX_FILE_SIZE);
    }
    
    /**
     * Configures the logger with the given settings.
     *
     * @param logFolderPath
     * @param fileName
     * @param logLevel
     * @param logPattern
     */
    public static void log4JConfigure(final String logFolderPath, final String fileName, final LogType logLevel, final String logPattern) {
        log4JConfigure(logFolderPath, fileName, logLevel, logPattern, MAX_BACKUP_FILES, MAX_FILE_SIZE);
    }
    
    /**
     * Configures the logger with the given settings.
     *
     * @param logFolderPath
     * @param fileName
     * @param logLevel
     */
    public static void log4JConfigure(final String logFolderPath, final String fileName, final LogType logLevel) {
        log4JConfigure(logFolderPath, fileName, logLevel, LOG_PATTERN, MAX_BACKUP_FILES, MAX_FILE_SIZE);
    }
    
    /**
     * Creates the logs in the given logFileName under the parentFolder.
     *
     * @param logFolderPath
     * @param logLevel
     */
    public static void log4JConfigure(final String logFolderPath, final LogType logLevel) {
        log4JConfigure(logFolderPath, LOG_FILE_NAME, logLevel, LOG_PATTERN, MAX_BACKUP_FILES, MAX_FILE_SIZE);
    }
    
    /**
     * Creates the logs in the given logFolderPath under the /Android/data folder.
     *
     * @param logFolderPath
     */
    public static void log4JConfigure(final String logFolderPath) {
        log4JConfigure(logFolderPath, LOG_FILE_NAME, LogType.INFO, LOG_PATTERN, MAX_BACKUP_FILES, MAX_FILE_SIZE);
    }
    
    /**
     * Initializes the logger with the <code>log4JFileStream</code> input stream.
     * If the <code>log4JFileStream</code> is null or empty then the <code>useXMLConfig</code>
     * property is checked to load the default configuration file. if <code>useXMLConfig</code> is
     * set to be true, then the <code>android_log4j.xml</code> file is used otherwise the
     * <code>android_log4j.properties</code> is used to configure the logger.
     *
     * @param logFolderPath
     * @param context
     * @param log4JFileStream
     * @param useXMLConfig
     */
    public static void log4JConfigure(final String logFolderPath, final Context context, InputStream log4JFileStream, final boolean useXMLConfig) {
        setLog4JLogsEnabled(true);
        if(isNull(log4JFileStream)) {
            if(useXMLConfig) {
                log4JFileStream = readAssets(context, ANDROID_LOG4J_XML);
            } else {
                log4JFileStream = readAssets(context, ANDROID_LOG4J_PROPERTIES);
            }
        }
        
        if(isNull(log4JFileStream)) {
            throw new RuntimeException((useXMLConfig ? ANDROID_LOG4J_XML : ANDROID_LOG4J_PROPERTIES) + " configuration file did not find!");
        }
        
        if(useXMLConfig && false) {
            /* TODO - Find a way to override the log file path based on the specified logFileName. */
            /* loads the provided log4j file. */
            PropertyConfigurator.configure(log4JFileStream);
        } else {
            final Properties mProperties = loadProperties(log4JFileStream);
            if(mProperties.size() == 0) {
                throw new RuntimeException("Invalid [" + ANDROID_LOG4J_PROPERTIES + "] configuration file!");
            }
            setLog4JLogsEnabled(true);
            /* setting all the properties in the reverse order. */
            /** the logs folder to be set. */
            sLog4JConfigurator.setLogsFolder(logFolderPath);
            
            /** the log file name to be set. */
            sLog4JConfigurator.setFileName(mProperties.getProperty(KEY_RFA_FILE, LOG_FILE_NAME));
            
            /** the root log level to be set. */
            String rootLogger = mProperties.getProperty(LOG4J_ROOT_LOGGER, LogType.INFO.toString());
            if(isNullOrEmpty(rootLogger) || rootLogger.length() < 8) {
                throw new RuntimeException("Invalid [" + ANDROID_LOG4J_PROPERTIES + "] file! rootLogger:" + rootLogger);
            } else {
                rootLogger = rootLogger.split(",")[0];
            }
            sLog4JConfigurator.setLogLevel(LogType.toLevel(LogType.valueOf(rootLogger)));
            
            /** the logs pattern to be set. */
            sLog4JConfigurator.setLogPattern(mProperties.getProperty(KEY_RFA_LOG_PATTERN, LOG_PATTERN));
            
            /** the maximum number of backup files to be created. */
            sLog4JConfigurator.setMaxBackupFiles(Integer.parseInt(mProperties.getProperty(KEY_RFA_MAX_BACKUP_FILES, String.valueOf(MAX_BACKUP_FILES))));
            
            /** the maximum log file size to be set. */
            sLog4JConfigurator.setMaxFileSize(Integer.parseInt(mProperties.getProperty(KEY_RFA_MAX_FILE_SIZE, "2")) * MB_SIZE);
            
            //configure the log4j logger
            sLog4JConfigurator.configure();
//            PropertyConfigurator.configure(loadProperties(log4JFileStream));
        }
        
    }
    
    /**
     * Initializes the logger with the <code>xmlLog4JStream</code> input stream.
     * If the <code>xmlLog4JStream</code> is null or empty then the <code>android_log4j.properties</code>
     * is used to configure the logger.
     *
     * @param context
     * @param log4JFileStream
     */
    public static void log4JConfigure(final String logFolderPath, final Context context, InputStream log4JFileStream) {
        log4JConfigure(logFolderPath, context, log4JFileStream, false);
    }
    
    
    /**************************************************************************
     * Log4J Logger Helper methods.
     **************************************************************************/
    
    /**
     * Returns the <code>Logger</code> object for the specified
     * <code>logClass</code> class.
     *
     * @param logClass
     * @return
     */
    public static final Logger getLogger(final Class<?> logClass) {
        if(isNull(logClass)) {
            throw new IllegalArgumentException("logClass is NULL! it must provide!");
        } else {
            return Logger.getLogger(logClass);
        }
    }
    
    /**
     * Returns the <code>Logger</code> object for the specified
     * <code>logClassName</code> class name.
     * <p>
     *
     * @param logClassName
     * @return
     */
    public static final Logger getLogger(final String logClassName) {
        if(isNull(logClassName)) {
            throw new IllegalArgumentException("logClass is NULL! it must provide!");
        } else {
            return Logger.getLogger(logClassName);
        }
    }
    
    /**
     * The <code>logType</code> is to set for the <code>Logger</code> object for the specified
     * <code>logClass</code> class.
     *
     * @param logClass
     * @param logType
     * @return
     */
    public static final void setLogTypeFor(final Class<?> logClass, final LogType logType) {
        getLogger(logClass).setLevel(LogType.toLevel(logType));
    }
    
    /**
     * The <code>logType</code> is to set for the <code>Logger</code> object for the specified
     * <code>logClassName</code> class.
     *
     * @param logClassName
     * @param logType
     */
    public final void setLogTypeFor(final String logClassName, final LogType logType) {
        getLogger(logClassName).setLevel(LogType.toLevel(logType));
    }
    
    /**
     * Returns true if the log-level is >= Level.INFO otherwise false.
     *
     * @return
     */
    public static boolean isLogEnabledForProduction() {
        return isLogEnabledFor(LogType.INFO);
    }
    
    /**************************************************************************
     * Log Helper Methods.
     **************************************************************************/
    
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
                Log.e(logTag, toString(logMessage));
            }
        }
    }
    
    /**
     * Logs ERROR messages.
     *
     * @param logTag
     * @param logMessage
     * @param throwable
     */
    public static void e(final String logTag, final String logMessage, final Throwable throwable) {
        if(isLogEnabledFor(LogType.ERROR)) {
            if(isLog4JLogsEnabled()) {
                getLogger(logTag).error(logMessage, throwable);
            } else {
                Log.e(logTag, toString(logMessage), throwable);
            }
        }
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
                Log.w(logTag, toString(logMessage));
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
                Log.i(logTag, toString(logMessage));
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
                getLogger(logTag).debug(logMessage);
            } else {
                Log.d(logTag, toString(logMessage));
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
                Log.v(logTag, toString(logMessage));
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
        e(logTag, throwable.getLocalizedMessage(), throwable);
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
        e(logTag, format(format, logArguments), throwable);
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
