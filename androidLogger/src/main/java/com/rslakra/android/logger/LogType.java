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

import org.apache.log4j.Level;

/**
 * This ENUM handles the logTypes supported by <code>Logger</code>.
 *
 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
 * @version 1.0.0
 * @created 2015-11-16 06:55:24 PM
 * @since 1.0.0
 */
public enum LogType {
    /* ASSERT type is for future usage. Don't use it. */
    SUPPRESS, ASSERT, ERROR, WARN, INFO, DEBUG, VERBOSE;
    
    /**
     * Returns true if the <code>logType</code> equals to <code>logTypeString</code> otherwise
     * false. If the <code>ignoreCase</code> is set to be true, the result is based on ignoring the
     * case.
     *
     * @param logType
     * @param logTypeString
     * @param ignoreCase
     * @return
     */
    public static boolean isEqual(final LogType logType, final String logTypeString, final boolean ignoreCase) {
        if(LogHelper.isNull(logType) || LogHelper.isNullOrEmpty(logTypeString)) {
            return false;
        } else if(ignoreCase) {
            return (logType.toString().toLowerCase().equals(logTypeString.toLowerCase()));
        } else {
            return (logType.toString().equals(logTypeString));
        }
    }
    
    /**
     * Returns true if the <code>logType</code> equals to <code>logTypeString</code> otherwise
     * false. This result is based on ignoring the case.
     *
     * @param logType
     * @param logTypeString
     * @return
     */
    public static boolean isEqual(final LogType logType, final String logTypeString) {
        return isEqual(logType, logTypeString, true);
    }
    
    /**
     * Returns the <code>LogType</code> for the given <code>logType</code> string.
     *
     * @param logTypeString
     * @return
     */
    public static LogType toLogType(final String logTypeString) {
        final LogType[] logTypes = LogType.values();
        for(LogType logType : logTypes) {
            if(isEqual(logType, logTypeString)) {
                return logType;
            }
        }
        
        throw new RuntimeException("Invalid logType:" + logTypeString);
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
            return SUPPRESS;
        } else if(logLevel == Level.ERROR) {
            return ERROR;
        } else if(logLevel == Level.WARN) {
            return WARN;
        } else if(logLevel == Level.INFO) {
            return INFO;
        } else if(logLevel == Level.DEBUG) {
            return DEBUG;
        } else if(logLevel == Level.TRACE) {
            return VERBOSE;
        } else {
            throw new RuntimeException("Invalid logLevel:" + logLevel);
        }
    }
}
