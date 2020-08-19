/**
 * 
 */
package com.github.jerryxia.devhelper.util;

import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class ExceptionCatchTest {

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
                try {
                    int j = 1 / 0;
                } catch (Exception e) {
                    throw e;
                }
            } catch(Exception e) {
                
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

}
