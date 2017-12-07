/**
 * 
 */
package com.github.jerryxia.devhelper.util;

/**
 * @author guqk
 *
 */
public class StopWatch {
    private static final long NANO_TO_MIlLI = 1000000L;
    private long              elapsed;
    private boolean           running;
    private long              startNanoTime;

    public StopWatch() {
        this.reset();
    }

    /**
     * Initializes a new Stopwatch instance, sets the elapsed time property to zero, and starts measuring elapsed time.
     * 
     * @return a new StopWatch instance
     */
    public static StopWatch startNew() {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        return stopwatch;
    }

    /**
     * Stops time interval measurement and resets the elapsed time to zero.
     */
    public void reset() {
        this.elapsed = 0L;
        this.running = false;
        this.startNanoTime = 0L;
    }

    /**
     * Stops time interval measurement, resets the elapsed time to zero, and starts measuring elapsed time.
     */
    public void restart() {
        this.elapsed = 0L;
        this.startNanoTime = SystemClock.nanoTime();
        this.running = true;
    }

    /**
     * Starts, or resumes, measuring elapsed time for an interval.
     */
    public void start() {
        if (!this.running) {
            this.startNanoTime = SystemClock.nanoTime();
            this.running = true;
        }
    }

    /**
     * Stops measuring elapsed time for an interval.
     * 
     */
    public void stop() {
        if (this.running) {
            long timestamp = SystemClock.nanoTime();
            long num = timestamp - this.startNanoTime;
            this.elapsed += num;
            this.running = false;
            if (this.elapsed < 0L) {
                this.elapsed = 0L;
            }
        }
    }

    /**
     * Gets the total elapsed time measured by the current instance, in milliseconds.
     * 
     * @return
     */
    public long elapsedMilliseconds() {
        return this.elapsedNanoSeconds() / NANO_TO_MIlLI;
    }

    /**
     * Gets the total elapsed time measured by the current instance, in timer ticks.
     * 
     * @return
     */
    public long elapsedNanoSeconds() {
        long num = this.elapsed;
        if (this.running)
        {
            long timestamp = SystemClock.nanoTime();
            long num2 = timestamp - this.startNanoTime;
            num += num2;
        }
        return num;
    }

    /**
     * Gets a value indicating whether the Stopwatch timer is running.
     * 
     * @return
     */
    public boolean isRunning() {
        return this.running;
    }
}