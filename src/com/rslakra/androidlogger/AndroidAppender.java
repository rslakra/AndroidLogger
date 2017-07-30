package com.rslakra.androidlogger;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import android.util.Log;

/**
 * An Android log4j appender.
 * 
 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
 * @created 2017-07-29 02:09:05 PM
 * @version 1.0.0
 * @since 1.0.0
 */
public class AndroidAppender extends AppenderSkeleton {

	/* tagLayout */
	private Layout tagLayout;

	/**
	 * The constructor with parameters.
	 * 
	 * @param tagLayout
	 * @param layoutMessage
	 */
	public AndroidAppender(final Layout tagLayout, final Layout layoutMessage) {
		this.tagLayout = tagLayout;
		setLayout(layoutMessage);
	}

	/**
	 * Returns the tagLayout.
	 *
	 * @return
	 */
	public Layout getTagLayout() {
		return tagLayout;
	}

	/**
	 * The tagLayout to be set.
	 *
	 * @param tagLayout
	 */
	public void setTagLayout(final Layout tagLayout) {
		this.tagLayout = tagLayout;
	}

	/**
	 * Appends the logging event.
	 * 
	 * @param logEvent
	 */
	@Override
	protected void append(final LoggingEvent logEvent) {
		switch (logEvent.getLevel().toInt()) {
		case Level.ALL_INT:
			if (logEvent.getThrowableInformation() != null) {
				Log.v(getTagLayout().format(logEvent), getLayout().format(logEvent),
						logEvent.getThrowableInformation().getThrowable());
			} else {
				Log.v(getTagLayout().format(logEvent), getLayout().format(logEvent));
			}
			break;
		case Level.DEBUG_INT:
			if (logEvent.getThrowableInformation() != null) {
				Log.d(getTagLayout().format(logEvent), getLayout().format(logEvent),
						logEvent.getThrowableInformation().getThrowable());
			} else {
				Log.d(getTagLayout().format(logEvent), getLayout().format(logEvent));
			}
			break;
		case Level.INFO_INT:
			if (logEvent.getThrowableInformation() != null) {
				Log.i(getTagLayout().format(logEvent), getLayout().format(logEvent),
						logEvent.getThrowableInformation().getThrowable());
			} else {
				Log.i(getTagLayout().format(logEvent), getLayout().format(logEvent));
			}
			break;
		case Level.WARN_INT:
			if (logEvent.getThrowableInformation() != null) {
				Log.w(getTagLayout().format(logEvent), getLayout().format(logEvent),
						logEvent.getThrowableInformation().getThrowable());
			} else {
				Log.w(getTagLayout().format(logEvent), getLayout().format(logEvent));
			}
			break;
		case Level.ERROR_INT:
			if (logEvent.getThrowableInformation() != null) {
				Log.e(getTagLayout().format(logEvent), getLayout().format(logEvent),
						logEvent.getThrowableInformation().getThrowable());
			} else {
				Log.e(getTagLayout().format(logEvent), getLayout().format(logEvent));
			}
			break;
		case Level.FATAL_INT:
			if (logEvent.getThrowableInformation() != null) {
				Log.wtf(getTagLayout().format(logEvent), getLayout().format(logEvent),
						logEvent.getThrowableInformation().getThrowable());
			} else {
				Log.wtf(getTagLayout().format(logEvent), getLayout().format(logEvent));
			}
			break;
		}
	}

	/**
	 * 
	 * @see org.apache.log4j.AppenderSkeleton#close()
	 */
	@Override
	public void close() {
	}

	/**
	 * 
	 * @return
	 * @see org.apache.log4j.AppenderSkeleton#requiresLayout()
	 */
	@Override
	public boolean requiresLayout() {
		return true;
	}

}
