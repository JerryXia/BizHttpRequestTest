/**
 * 
 */
package com.github.jerryxia.devhelper.event;

import java.util.HashMap;

import com.lmax.disruptor.EventHandler;

public class ValueWrapperEventDispatchHandler implements EventHandler<ValueWrapperEvent> {
    private final HashMap<Class<?>, EventHandler<ValueWrapperEvent>> eventHandlers;
    private final MutiSourceValueWrapperEventStat                    eventStats;

    public ValueWrapperEventDispatchHandler(MutiSourceValueWrapperEventStat eventStats) {
        if (eventStats == null) {
            throw new IllegalArgumentException("parameter:'eventStats' can't be null.");
        }
        this.eventStats = eventStats;
        this.eventHandlers = new HashMap<Class<?>, EventHandler<ValueWrapperEvent>>();
    }

    @Override
    public void onEvent(ValueWrapperEvent event, long sequence, boolean endOfBatch) {
        Class<?> source = event.getValue().getClass();
        EventHandler<ValueWrapperEvent> eventHandler = this.eventHandlers.get(source);
        if (eventHandler != null) {
            try {
                eventHandler.onEvent(event, sequence, endOfBatch);
                eventStats.allocate(source).incrementConsumerSuccessCount();
            } catch (Exception e) {
                eventStats.allocate(source).incrementConsumerFailCount();
            }
        }
    }

    public synchronized void registerHander(Class<?> source, EventHandler<ValueWrapperEvent> eventHandler) {
        if (source != null && eventHandler != null) {
            this.eventHandlers.put(source, eventHandler);
        }
    }
}