/**
 * 
 */
package com.github.jerryxia.devhelper.event;

import com.lmax.disruptor.ExceptionHandler;

public class ValueWrapperEventExceptionHandler implements ExceptionHandler<ValueWrapperEvent> {
    @Override
    public void handleEventException(Throwable ex, long sequence, ValueWrapperEvent event) {
        try {
            // Careful to avoid allocation in case of memory pressure.
            // Sacrifice performance for safety by writing directly rather than using a buffer.
            System.err.print("com.github.jerryxia.devhelper.event.ValueWrapperEventExceptionHandler exception processing: ");
            System.err.print("event seq=");
            System.err.print(sequence);
            System.err.print(", value='");
            System.err.print(event);
            System.err.println("'");
            // Attempt to print the full stack trace, which may fail if we're already OOMing
            // We've already provided sufficient information at this point.
            ex.printStackTrace();
        } catch (final Throwable ignored) {
            // LOG4J2-2333: Not much we can do here without risking an OOM.
            // Throwing an error here may kill the background thread.
        }
    }

    @Override
    public void handleOnStartException(final Throwable throwable) {
        System.err.println("Exception during onStart()");
        throwable.printStackTrace();
    }

    @Override
    public void handleOnShutdownException(final Throwable throwable) {
        System.err.println("Exception during onShutdown()");
        throwable.printStackTrace();
    }
}
