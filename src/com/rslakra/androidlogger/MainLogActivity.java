package com.rslakra.androidlogger;

import com.rslakra.androidlogger.AndroidLogger.LogType;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * The main logger activity.
 * 
 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
 * @created 2017-08-26 03:54:25 PM
 * @version 1.0.0
 * @since 1.0.0
 */
public class MainLogActivity extends Activity {

	/* LOG_TAG */
	private static final String LOG_TAG = "MainLogActivity";

	private TextView mLogsTextView;

	/**
	 * 
	 * @param savedInstanceState
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_log);
		mLogsTextView = (TextView) findViewById(R.id.tvLogs);

		// Configure Android Logger
		AndroidLogger.log4jConfigure();
		mLogsTextView.append(LogType.VERBOSE.toString());
		AndroidLogger.v(LOG_TAG, LogType.VERBOSE.toString());
		AndroidLogger.d(LOG_TAG, LogType.DEBUG.toString());
		AndroidLogger.i(LOG_TAG, LogType.INFO.toString());
		AndroidLogger.w(LOG_TAG, LogType.WARN.toString());
		AndroidLogger.e(LOG_TAG, LogType.ERROR.toString());
	}

	/**
	 * 
	 * @param menu
	 * @return
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_log, menu);
		return true;
	}

	/**
	 * 
	 * @param item
	 * @return
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
