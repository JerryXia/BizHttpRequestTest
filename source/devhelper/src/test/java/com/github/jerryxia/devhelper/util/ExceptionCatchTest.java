/**
 *
 */
package com.github.jerryxia.devhelper.util;

import ch.qos.logback.core.helpers.ThrowableToStringArray;
import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class ExceptionCatchTest {

    private void throwException() {
        try {
            int j = 1 / 0;
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void catch_exception_time_test() {
        int count = 100 * 10000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            try {
                int j = 1 / 0;
            } catch (Exception e) {

            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);

        startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            try {
                throwException();
            } catch (Exception e) {

            }
        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

    @Test
    public void test_print_error() {
        try {
            throwException();
        } catch (Exception t) {
            StringBuilder sb = new StringBuilder();

            String[] stringRep = ThrowableToStringArray.convert(t);
            for (String s : stringRep) {
                System.out.println(s);
            }
        }
    }
}
