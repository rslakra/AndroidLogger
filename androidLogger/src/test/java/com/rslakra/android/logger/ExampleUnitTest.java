package com.rslakra.android.logger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    
    private final String LOG_TYPE = "ExampleUnitTest";
    
    @Before
    public void configure() {
        final String logFolderPath = LogHelper.pathString(LogHelper.getUserDir(), "logs");
        LogHelper.log4JConfigure(logFolderPath, LogType.DEBUG);
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isDebugEnabled() throws Exception {
        LogHelper.setLogType(LogType.DEBUG);
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.DEBUG));
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isInfoEnabled() throws Exception {
        LogHelper.setLogType(LogType.INFO);
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.INFO));
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isWarnEnabled() throws Exception {
        LogHelper.setLogType(LogType.WARN);
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.WARN));
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isErrorEnabled() throws Exception {
        LogHelper.setLogType(LogType.ERROR);
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.ERROR));
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isLogDisabled() throws Exception {
        LogHelper.setLogType(LogType.SUPPRESS);
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.SUPPRESS));
    }
    
}