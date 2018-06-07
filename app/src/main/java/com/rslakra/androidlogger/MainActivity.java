package com.rslakra.androidlogger;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.rslakra.android.logger.LogHelper;
import com.rslakra.android.logger.LogType;

/**
 * The main logger activity.
 *
 * @author Rohtash Lakra (rohtash.lakra@devamatre.com)
 * @author Rohtash Singh Lakra (rohtash.singh@gmail.com)
 * @version 1.0.0
 * @created 2017-08-26 03:54:25 PM
 * @since 1.0.0
 */
public class MainActivity extends Activity {
    
    /* LOG_TAG */
    private static final String LOG_TAG = "MainActivity";
    
    private TextView mLogTextView;
    
    /**
     * @param savedInstanceState
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogTextView = (TextView) findViewById(R.id.logTextView);
        //add this line to make the TextView scrollable.
        mLogTextView.setMovementMethod(new ScrollingMovementMethod());
        
        // Configure Android Logger
        final String logFolderPath = LogHelper.pathString(LogHelper.getAppRootFolder(getApplicationContext()), "logs");
//        LogHelper.log4JConfigure(logFolderPath, LogType.INFO);
        LogHelper.log4JConfigure(logFolderPath, "AndroidLogger.log", LogType.INFO);
//        LogHelper.log4JConfigure(logFolderPath, getApplicationContext(), LogHelper.ANDROID_LOG4J_PROPERTIES);
        
        //test file logger
        LogHelper.i(LOG_TAG, "Log File Path:" + LogHelper.getLogFilePath());
        LogHelper.i(LOG_TAG, LogHelper.getLineSeparator());
        LogHelper.i(LOG_TAG, "Testing Android Logger.");
        LogHelper.i(LOG_TAG, LogHelper.getLineSeparator());
        testLogger();
        LogHelper.i(LOG_TAG, LogHelper.getLineSeparator());
        LogHelper.i(LOG_TAG, "End of Logs!");
        
        //test text view logger
        showLoggedLogs();
    }
    
    /**
     * Tests the dummy logging.
     */
    private void testLogger() {
        //log config details.
        LogHelper.v(LOG_TAG, LogType.VERBOSE.toString());
        LogHelper.d(LOG_TAG, LogType.DEBUG.toString());
        LogHelper.i(LOG_TAG, LogType.INFO.toString());
        LogHelper.w(LOG_TAG, LogType.WARN.toString());
        LogHelper.e(LOG_TAG, LogType.ERROR.toString());
    }
    
    /**
     * Shows the logged logs inside the text view.
     */
    private void showLoggedLogs() {
        final StringBuilder logBuilder = new StringBuilder();
        byte[] logFileBytes = LogHelper.readBytesFully(LogHelper.getLogFilePath());
        if(logFileBytes != null) {
            logBuilder.append(new String(logFileBytes));
        } else {
            logBuilder.append("No data loaded!");
        }
        
        mLogTextView.append(logBuilder.toString());
    }
    
    /**
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
        if(id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
