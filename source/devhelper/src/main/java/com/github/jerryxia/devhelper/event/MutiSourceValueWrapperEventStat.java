/**
 * 
 */
package com.github.jerryxia.devhelper.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class MutiSourceValueWrapperEventStat {
    private final HashMap<Class<?>, ValueWrapperEventStat> stats;

    public MutiSourceValueWrapperEventStat() {
        this.stats = new HashMap<Class<?>, ValueWrapperEventStat>();
    }

    /**
     * register one ValueWrapperEventStat
     * 
     * @param source
     * @return 'ValueWrapperEventStat' which is related with the source determined.
     */
    public synchronized ValueWrapperEventStat register(Class<?> source) {
        if (source == null) {
            return null;
        }
        ValueWrapperEventStat eventStat = this.stats.get(source);
        if (eventStat == null) {
            eventStat = new ValueWrapperEventStat();
            this.stats.put(source, eventStat);
        }
        return eventStat;
    }

    public ValueWrapperEventStat allocate(Class<?> source) {
        if (source == null) {
            return null;
        }
        ValueWrapperEventStat eventStat = this.stats.get(source);
        return eventStat;
    }

    public void resetAll() {
        Iterator<Entry<Class<?>, ValueWrapperEventStat>> entrySetIterator = stats.entrySet().iterator();
        while (entrySetIterator.hasNext()) {
            Entry<Class<?>, ValueWrapperEventStat> entry = entrySetIterator.next();
            entry.getValue().reset();
        }
    }
}
