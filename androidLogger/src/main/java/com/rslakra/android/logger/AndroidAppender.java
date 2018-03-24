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