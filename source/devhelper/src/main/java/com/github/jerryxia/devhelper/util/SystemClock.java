/**
 * 
 */
package com.github.jerryxia.devhelper.util;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 代替系统的时钟
 * 
 * @author guqk
 *
 */
public class SystemClock {
    private final long period;
    private volatile long now;

    private static final SystemClock INSTANCE = new SystemClock(1);

    private SystemClock(long period) {
        this.period = period;
        // this.now = new AtomicLong(System.currentTimeMillis());
        this.now = System.currentTimeMillis();
        this.scheduleClockUpdating();
    }

    public static long now() {
        return INSTANCE.currentTimeMillis();
    }

    public static Date nowDate() {
        return new Date(INSTANCE.currentTimeMillis());
    }

    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable, "devhelper-util-SystemClock");
                thread.setDaemon(true);
                return thread;
            }
        });
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                now = System.currentTimeMillis();
            }
        }, period, period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return this.now;
    }
}