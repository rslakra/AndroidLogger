package com.rslakra.androidlogger;

import android.app.Activity;
import android.os.Bundle;
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
        
        // Configure Android Logger
        final String logFolderPath = LogHelper.pathString(LogHelper.getAppRootFolder(getApplicationContext()), "logs");
        LogHelper.log4jConfigure(logFolderPath, LogType.VERBOSE);
        
        //test file logger
        testFileLogger();
        
        //test text view logger
        testTextViewLogger(logFolderPath);
    }
    
    /**
     * Test Logger.
     */
    private void testFileLogger() {
        //log config details.
        LogHelper.v(LOG_TAG, LogType.VERBOSE.toString());
        LogHelper.d(LOG_TAG, LogType.DEBUG.toString());
        LogHelper.i(LOG_TAG, LogType.INFO.toString());
        LogHelper.w(LOG_TAG, LogType.WARN.toString());
        LogHelper.e(LOG_TAG, LogType.ERROR.toString());
    }
    
    /**
     * Test TextView Logger.
     *
     * @param logFolderPath
     */
    private void testTextViewLogger(final String logFolderPath) {
        final StringBuilder logBuilder = new StringBuilder();
        logBuilder.append("Logs Folder:" + logFolderPath);
        logBuilder.append(LogHelper.getLineSeparator());
        logBuilder.append("Logs Enabled");
        logBuilder.append(LogHelper.getLineSeparator());
        
        //check VERBOSE
        if(LogHelper.isLogEnabledFor(LogType.VERBOSE)) {
            logBuilder.append(LogType.VERBOSE.toString());
            logBuilder.append(LogHelper.getLineSeparator());
        }
        
        //check DEBUG
        if(LogHelper.isLogEnabledFor(LogType.DEBUG)) {
            logBuilder.append(LogType.DEBUG.toString());
            logBuilder.append(LogHelper.getLineSeparator());
        }
        
        //check INFO
        if(LogHelper.isLogEnabledFor(LogType.INFO)) {
            logBuilder.append(LogType.INFO.toString());
            logBuilder.append(LogHelper.getLineSeparator());
        }
        
        //check WARN
        if(LogHelper.isLogEnabledFor(LogType.WARN)) {
            logBuilder.append(LogType.WARN.toString());
            logBuilder.append(LogHelper.getLineSeparator());
        }
        
        //check ERROR
        if(LogHelper.isLogEnabledFor(LogType.ERROR)) {
            logBuilder.append(LogType.ERROR.toString());
            logBuilder.append(LogHelper.getLineSeparator());
        }
        
        //check SUPPRESS
        if(LogHelper.isLogEnabledFor(LogType.SUPPRESS)) {
            logBuilder.append(LogType.SUPPRESS.toString());
            logBuilder.append(LogHelper.getLineSeparator());
        }
        
        logBuilder.append("End Logs.");
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
