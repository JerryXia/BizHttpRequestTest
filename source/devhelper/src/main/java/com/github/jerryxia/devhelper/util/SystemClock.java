/**
 * 
 */
package com.github.jerryxia.devhelper.util;

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
public final class SystemClock {
    private final long    period;
    private int           periodCount = 0;
    private volatile long now;
    private volatile long nanoTime;

    private static final SystemClock INSTANCE = new SystemClock(100);

    private SystemClock(long period) {
        this.period = period;
        this.now = System.currentTimeMillis();
        this.nanoTime = System.nanoTime();
        this.scheduleClockUpdating();
    }

    public static long now() {
        return INSTANCE.currentTimeMillis();
    }

    public static long nanoTime() {
        return INSTANCE.currentNanoTime();
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
                nanoTime = System.nanoTime();
                // 1微妙 10次 1毫秒10000次
                if (++periodCount == 10000) {
                    now = System.currentTimeMillis();
                    periodCount = 0;
                }
            }
        }, period, period, TimeUnit.NANOSECONDS);
    }

    private long currentTimeMillis() {
        return this.now;
    }

    private long currentNanoTime() {
        return this.nanoTime;
    }
}