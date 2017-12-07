/**
 * 
 */
package com.github.jerryxia.devhelper.util;

import org.junit.Test;

/**
 * @author guqk
 *
 */
public class SystemClockTest {

    @Test
    public void testIsOk() {
        long n0 = SystemClock.nanoTime();
        for(int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(SystemClock.now() / 1000);
        }
        long n1 = SystemClock.nanoTime();
        long n1_n0 = n1 - n0;
        System.out.println(n1_n0 / 1000000);
    }
}
