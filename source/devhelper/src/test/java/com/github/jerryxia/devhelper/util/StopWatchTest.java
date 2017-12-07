/**
 * 
 */
package com.github.jerryxia.devhelper.util;

import org.junit.Test;

/**
 * @author guqk
 *
 */
public class StopWatchTest {

    @Test
    public void testOneSeconds() {
        StopWatch sw = StopWatch.startNew();
        for(int i = 0; i < 1; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sw.elapsedMilliseconds());
    }
    
    @Test
    public void testTenSeconds() {
        StopWatch sw = StopWatch.startNew();
        for(int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(sw.elapsedMilliseconds());
    }
}
