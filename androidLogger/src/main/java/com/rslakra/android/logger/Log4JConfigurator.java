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

import android.util.Log;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggerRepository;

import java.io.File;
import java.io.IOException;

/**
 * This class handles the log4j configuration for Android.
 * <p>
 * Configures the Log4j logging framework.
 * See <a href=
 * "http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/PatternLayout.html">Patterns</a>
 * for pattern layout.
 *
 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
 * @version 1.0.0
 * @created 2015-11-16 06:55:24 PM
 * @since 1.0.0
 */
public final class Log4JConfigurator {
    
    /** LOG_TAG */
    private static final String LOG_TAG = "Log4JConfigurator";
    
    /** mLoggerRepository */
    private final LoggerRepository mLoggerRepository;
    
    /** mRootLogger */
    private Logger mRootLogger;
    
    /** mLogLevel */
    private Level mLogLevel = Level.INFO;
    
    /** mLogPattern */
    private PatternLayout mLogPattern;
    
    /** mLogsFolder */
    private String mLogsFolder;
    
    /** mFileName */
    private String mFileName;
    
    /** mLogFilePath */
    private String mLogFilePath;
    
    /** mMaxBackupFiles */
    private int mMaxBackupFiles;
    
    /** mMaxFileSize */
    private long mMaxFileSize;
    
    /** mImmediateFlush */
    private boolean mImmediateFlush;
    
    /** mInternalLogging */
    private boolean mInternalLogging;
    
    /** mResetConfiguration */
    private boolean mResetConfiguration;
    
    /** mUseConsoleAppender */
    private boolean mUseConsoleAppender;
    
    /** mUseFileAppender */
    private boolean mUseFileAppender;
    
    /** mUseRollingFileAppender */
    private boolean mUseRollingFileAppender;
    
    /** mConsoleAppender */
    private AndroidAppender mConsoleAppender;
    
    /** mFileAppender */
    private FileAppender mFileAppender;
    
    /**
     * Default Constructor.
     */
    public Log4JConfigurator() {
        mLoggerRepository = LogManager.getLoggerRepository();
        setImmediateFlush(true);
        setInternalLogging(false);
        setResetConfiguration(true);
        setUseConsoleAppender(true);
        setUseFileAppender(true);
        setUseRollingFileAppender(true);
    }
    
    /**
     * Returns the <code>mLoggerRepository</code> value.
     *
     * @return
     */
    public final LoggerRepository getLoggerRepository() {
        return mLoggerRepository;
    }
    
    /**
     * Returns the singleton instance of the root logger.
     *
     * @return
     */
    private final Logger getRootLogger() {
        if(mRootLogger == null) {
            synchronized(Log4JConfigurator.class) {
                if(mRootLogger == null) {
                    if(isResetConfiguration()) {
                        getLoggerRepository().resetConfiguration();
                    }
                    mRootLogger = getLoggerRepository().getRootLogger();
                    mRootLogger.setLevel(getLogLevel());
                }
            }
        }
        
        return mRootLogger;
    }
    
    /**
     * Returns the <code>mLogLevel</code> value of the root logger.
     *
     * @return Log level of the root logger
     */
    public final Level getLogLevel() {
        return mLogLevel;
    }
    
    /**
     * The <code>mLogLevel</code> to be set for the root logger.
     *
     * @param logLevel Log level for the root logger
     */
    public final void setLogLevel(final Level logLevel) {
        if(logLevel == null) {
            throw new NullPointerException("The root logLevel should not be NULL!");
        }
        
        if(this.mLogLevel != logLevel) {
            this.mLogLevel = logLevel;
        }
    }
    
    /**
     * Returns the <code>mLogPattern</code> value.
     *
     * @return
     */
    public final PatternLayout getLogPattern() {
        return mLogPattern;
    }
    
    /**
     * The <code>mLogPattern</code> to be set.
     *
     * @param logPattern
     */
    public final void setLogPattern(final PatternLayout logPattern) {
        if(LogHelper.isNull(logPattern)) {
            throw new IllegalArgumentException("logPattern is NULL!");
        }
        
        //initialize log pattern layout.
        if(this.mLogPattern != logPattern) {
            this.mLogPattern = logPattern;
        }
    }
    
    /**
     * The <code>mLogPattern</code> to be set.
     *
     * @param logPattern
     */
    public final void setLogPattern(final String logPattern) {
        if(LogHelper.isNullOrEmpty(logPattern)) {
            throw new IllegalArgumentException("logPattern is either NULL or EMPTY!");
        }
        
        //initialize log pattern layout.
        setLogPattern(new PatternLayout(logPattern));
    }
    
    /**
     * Returns the <code>mLogsFolder</code> value, which is the logs folder.
     *
     * @return
     */
    public final String getLogsFolder() {
        return mLogsFolder;
    }
    
    /**
     * The <code>mLogsFolder</code> to be set.
     *
     * @param logsFolder path of the logs folder.
     */
    public final void setLogsFolder(final String logsFolder) {
        if(LogHelper.isNullOrEmpty(logsFolder)) {
            throw new IllegalArgumentException("logsFolder is either NULL or EMPTY!");
        }
        
        this.mLogsFolder = logsFolder;
    }
    
    /**
     * Returns the <code>mFileName</code>, in which logs are stored.
     *
     * @return the name of the log file
     */
    public final String getFileName() {
        return mFileName;
    }
    
    /**
     * The <code>mFileName</code> to be set, in which logs are stored.
     *
     * @param fileName name of the log file
     */
    public final void setFileName(final String fileName) {
        if(LogHelper.isNullOrEmpty(fileName)) {
            throw new IllegalArgumentException("fileName is either NULL or EMPTY!");
        }
        
        this.mFileName = fileName;
    }
    
    /**
     * Returns the <code>mLogFilePath</code> value, in which, the logs are stored on the device.
     *
     * @return
     */
    public final String getLogFilePath() {
        if(LogHelper.isNullOrEmpty(mLogFilePath)) {
            mLogFilePath = LogHelper.pathString(getLogsFolder(), getFileName());
        }
        
        return mLogFilePath;
    }
    
    /**
     * Returns the maximum number of backed up log files
     *
     * @return Maximum number of backed up log files
     */
    public final int getMaxBackupFiles() {
        return mMaxBackupFiles;
    }
    
    /**
     * Sets the maximum number of backed up log files
     *
     * @param maxBackupFiles Maximum number of backed up log files
     */
    public final void setMaxBackupFiles(final int maxBackupFiles) {
        if(maxBackupFiles <= 0 || maxBackupFiles >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid Value! maxBackupFiles:" + maxBackupFiles);
        }
        this.mMaxBackupFiles = maxBackupFiles;
    }
    
    /**
     * Returns the maximum size of log file until rolling
     *
     * @return Maximum size of log file until rolling
     */
    public final long getMaxFileSize() {
        return mMaxFileSize;
    }
    
    /**
     * Sets the maximum size of log file until rolling
     *
     * @param maxFileSize Maximum size of log file until rolling
     */
    public final void setMaxFileSize(final long maxFileSize) {
        if(maxFileSize <= 0 || maxFileSize >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid Value! maxFileSize:" + maxFileSize);
        }
        this.mMaxFileSize = maxFileSize;
    }
    
    /**
     * Returns the immediateFlush.
     *
     * @return
     */
    public final boolean isImmediateFlush() {
        return mImmediateFlush;
    }
    
    /**
     * The immediateFlush to be set.
     *
     * @param immediateFlush
     */
    public final void setImmediateFlush(final boolean immediateFlush) {
        this.mImmediateFlush = immediateFlush;
    }
    
    /**
     * Returns the internalLogging.
     *
     * @return
     */
    public final boolean isInternalLogging() {
        return mInternalLogging;
    }
    
    /**
     * The internalLogging to be set.
     *
     * @param internalLogging
     */
    public final void setInternalLogging(final boolean internalLogging) {
        this.mInternalLogging = internalLogging;
    }
    
    /**
     * Resets the log4j configuration before applying this configuration.
     * Default is true.
     *
     * @return True, if the log4j configuration should be reset before applying this configuration.
     */
    public final boolean isResetConfiguration() {
        return mResetConfiguration;
    }
    
    /**
     * The resetConfiguration to be set.
     *
     * @param resetConfiguration
     */
    public final void setResetConfiguration(final boolean resetConfiguration) {
        this.mResetConfiguration = resetConfiguration;
    }
    
    /**
     * Returns the <code>mUseConsoleAppender</code> value.
     *
     * @return
     */
    public final boolean isUseConsoleAppender() {
        return mUseConsoleAppender;
    }
    
    /**
     * The <code>mUseConsoleAppender</code> to be set.
     *
     * @param useConsoleAppender
     */
    public final void setUseConsoleAppender(final boolean useConsoleAppender) {
        this.mUseConsoleAppender = useConsoleAppender;
    }
    
    /**
     * Returns the <code>mUseFileAppender</code> value.
     *
     * @return
     */
    public final boolean isUseFileAppender() {
        return mUseFileAppender;
    }
    
    /**
     * The <code>mUseFileAppender</code> to be set.
     *
     * @param useFileAppender
     */
    public final void setUseFileAppender(final boolean useFileAppender) {
        this.mUseFileAppender = useFileAppender;
    }
    
    /**
     * Returns the <code>mUseRollingFileAppender</code> value.
     *
     * @return
     */
    public final boolean isUseRollingFileAppender() {
        return mUseRollingFileAppender;
    }
    
    /**
     * The <code>mUseRollingFileAppender</code> to be set.
     *
     * @param useRollingFileAppender
     */
    public final void setUseRollingFileAppender(final boolean useRollingFileAppender) {
        this.mUseRollingFileAppender = useRollingFileAppender;
    }
    
    /**
     * Returns true if the current logLevel is >= the given logLevel otherwise false.
     *
     * @param logLevel
     * @return
     */
    public final boolean isLogEnabledFor(final Level logLevel) {
        return (logLevel != null && logLevel.toInt() >= getLogLevel().toInt());
    }
    
    /**
     * Configures the file appender based on the <code>mUseFileAppender</code> and the
     * <code>mUseRollingFileAppender</code> properties settings.
     */
    public final void addFileAppender() {
        if(isUseFileAppender()) {
            try {
                final File logFile = new File(getLogFilePath());
                /** Check logs file exists or not. */
                if(!logFile.exists()) {
                    /** Create logs folder, if it does not exist. */
                    if(!logFile.getParentFile().exists()) {
                        if(!logFile.getParentFile().mkdirs()) {
                            Log.w(LOG_TAG, "Unable to create folder:" + logFile.getParentFile().getAbsolutePath());
                        }
                    }
                    
                    /** Create log file, if it does not exist. */
                    if(!logFile.createNewFile()) {
                        Log.w(LOG_TAG, "Unable to create logs file:" + logFile.getAbsolutePath());
                    }
                }
                
                if(LogHelper.isNull(mFileAppender)) {
                    if(isUseRollingFileAppender()) {
                        mFileAppender = new RollingFileAppender(getLogPattern(), getLogFilePath());
                        ((RollingFileAppender) mFileAppender).setMaxBackupIndex(getMaxBackupFiles());
                        ((RollingFileAppender) mFileAppender).setMaximumFileSize(getMaxFileSize());
                        ((RollingFileAppender) mFileAppender).setImmediateFlush(isImmediateFlush());
                    } else {
                        mFileAppender = new FileAppender(getLogPattern(), getLogFilePath());
                    }
                }
                
                /** set file appender to root logger. */
                getRootLogger().addAppender(mFileAppender);
                Log.i(LOG_TAG, "Logs configured at:" + getLogFilePath());
            } catch(final IOException ex) {
                Log.e(LOG_TAG, ex.getLocalizedMessage(), ex);
                throw new RuntimeException("Error while configuring logging file appender!", ex);
            }
        } else {
            Log.w(LOG_TAG, "No file appender is configured!");
        }
    }
    
    /**
     * Configures the logger for file appender and android.
     */
    protected final void configure() {
        //reset configuration
        if(isResetConfiguration()) {
            getLoggerRepository().resetConfiguration();
        }
        
        //set internal logging.
        if(isInternalLogging()) {
            LogLog.setInternalDebugging(isInternalLogging());
        }
        
        // add android console logger
        if(isUseConsoleAppender()) {
            if(LogHelper.isNull(mConsoleAppender)) {
                mConsoleAppender = new AndroidAppender(getLogPattern());
            }
            getRootLogger().addAppender(mConsoleAppender);
        }
        
        //add file appender
        addFileAppender();
        
        //set log level for the root logger.
        getRootLogger().setLevel(getLogLevel());
    }
}