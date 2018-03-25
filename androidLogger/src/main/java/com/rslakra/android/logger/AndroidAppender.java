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

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;

import android.util.Log;

/**
 * An Android log4j appender.
 *
 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
 * @version 1.0.0
 * @created 2015-11-16 06:55:24 PM
 * @since 1.0.0
 */
public final class AndroidAppender extends AppenderSkeleton {
    
    /**
     * mTagLayout
     */
    private Layout mTagLayout;
    
    /**
     * @param tagLayout
     * @param logPattern
     */
    public AndroidAppender(final Layout tagLayout, final Layout logPattern) {
        this.mTagLayout = tagLayout;
        super.setLayout(logPattern);
    }
    
    /**
     * @param logPattern
     */
    public AndroidAppender(final Layout logPattern) {
        this(new PatternLayout("%c".intern()), logPattern);
    }
    
    
    /**
     * Appends the logs to the configured logger.
     *
     * @param logEvent
     */
    @Override
    protected void append(final LoggingEvent logEvent) {
        switch(logEvent.getLevel().toInt()) {
            case Level.TRACE_INT:
                if(logEvent.getThrowableInformation() != null) {
                    Log.v(getTagLayout().format(logEvent), getLayout().format(logEvent), logEvent.getThrowableInformation().getThrowable());
                } else {
                    Log.v(getTagLayout().format(logEvent), getLayout().format(logEvent));
                }
                break;
            case Level.DEBUG_INT:
                if(logEvent.getThrowableInformation() != null) {
                    Log.d(getTagLayout().format(logEvent), getLayout().format(logEvent), logEvent.getThrowableInformation().getThrowable());
                } else {
                    Log.d(getTagLayout().format(logEvent), getLayout().format(logEvent));
                }
                break;
            case Level.INFO_INT:
                if(logEvent.getThrowableInformation() != null) {
                    Log.i(getTagLayout().format(logEvent), getLayout().format(logEvent), logEvent.getThrowableInformation().getThrowable());
                } else {
                    Log.i(getTagLayout().format(logEvent), getLayout().format(logEvent));
                }
                break;
            case Level.WARN_INT:
                if(logEvent.getThrowableInformation() != null) {
                    Log.w(getTagLayout().format(logEvent), getLayout().format(logEvent), logEvent.getThrowableInformation().getThrowable());
                } else {
                    Log.w(getTagLayout().format(logEvent), getLayout().format(logEvent));
                }
                break;
            case Level.ERROR_INT:
                if(logEvent.getThrowableInformation() != null) {
                    Log.e(getTagLayout().format(logEvent), getLayout().format(logEvent), logEvent.getThrowableInformation().getThrowable());
                } else {
                    Log.e(getTagLayout().format(logEvent), getLayout().format(logEvent));
                }
                break;
            case Level.FATAL_INT:
                if(logEvent.getThrowableInformation() != null) {
                    Log.wtf(getTagLayout().format(logEvent), getLayout().format(logEvent), logEvent.getThrowableInformation().getThrowable());
                } else {
                    Log.wtf(getTagLayout().format(logEvent), getLayout().format(logEvent));
                }
                break;
        }
    }
    
    /**
     *
     */
    @Override
    public void close() {
    }
    
    /**
     * @return
     */
    @Override
    public boolean requiresLayout() {
        return true;
    }
    
    /**
     * Returns the tagLayout.
     *
     * @return
     */
    public Layout getTagLayout() {
        return mTagLayout;
    }
    
    /**
     * The tagLayout to be set.
     *
     * @param tagLayout
     */
    public void setTagLayout(final Layout tagLayout) {
        this.mTagLayout = tagLayout;
    }
}