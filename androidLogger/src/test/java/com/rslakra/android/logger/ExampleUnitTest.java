package com.rslakra.android.logger;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    
    /**
     * @throws Exception
     */
    @Test
    public void isDebugEnabled() throws Exception {
        assertEquals(false, LogHelper.isLogEnabledFor(LogType.DEBUG));
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isInfoEnabled() throws Exception {
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.INFO));
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isWarnEnabled() throws Exception {
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.WARN));
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isErrorEnabled() throws Exception {
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.ERROR));
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void isLogDisabled() throws Exception {
        assertEquals(true, LogHelper.isLogEnabledFor(LogType.SUPPRESS));
    }
    
}