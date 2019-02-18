/**
 * 
 */
package com.github.jerryxia.devhelper.elmah;


import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.github.jerryxia.devhelper.Bootstrapper;
import com.github.jerryxia.devhelper.Constants;

/**
 * @author Administrator
 *
 */
public class ErrorRecordFileStorageTest {
    @Test
    public void test_filter_is_ok() throws IOException {
        new Bootstrapper().init();
        
        ErrorRecordFileStorage errorRecordFileStorage = new ErrorRecordFileStorage(Constants.JAVA_IO_TMPDIR);
        errorRecordFileStorage.queryLimitData(null, 10);

        errorRecordFileStorage.queryLimitData("~DF731994C3D9DD9317.TMP", 10);

        
    }
}
