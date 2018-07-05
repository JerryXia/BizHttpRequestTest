/**
 * 
 */
package com.github.jerryxia.devhelper.requestcapture;

import com.lmax.disruptor.ExceptionHandler;

/**
 * @author Administrator
 *
 */
public class HttpRequestRecordEventExceptionHandler implements ExceptionHandler<HttpRequestRecordEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, HttpRequestRecordEvent event) {
        try {
            // Careful to avoid allocation in case of memory pressure.
            // Sacrifice performance for safety by writing directly
            // rather than using a buffer.
            System.err.print("HttpRequestRecordEventExceptionHandler error handling event seq=");
            System.err.print(sequence);
            System.err.print(", value='");
            try {
                System.err.print(event);
            } catch (Throwable t) {
                System.err.print("ERROR calling toString() on ");
                System.err.print(event.getClass().getName());
                System.err.print(": ");
                System.err.print(t.getClass().getName());
                System.err.print(": ");
                System.err.print(t.getMessage());
            }
            System.err.print("': ");
            System.err.print(ex.getClass().getName());
            System.err.print(": ");
            System.err.println(ex.getMessage());
            // Attempt to print the full stack trace, which may fail if we're already
            // OOMing We've already provided sufficient information at this point.
            ex.printStackTrace();
        } catch (final Throwable ignored) {
            // LOG4J2-2333: Not much we can do here without risking an OOM.
            // Throwing an error here may kill the background thread.
        }
    }

    @Override
    public void handleOnStartException(final Throwable throwable) {
        System.err.println("com.github.jerryxia.devhelper.requestcapture error starting:");
        throwable.printStackTrace();
    }

    @Override
    public void handleOnShutdownException(final Throwable throwable) {
        System.err.println("com.github.jerryxia.devhelper.requestcapture error shutting down:");
        throwable.printStackTrace();
    }
}
