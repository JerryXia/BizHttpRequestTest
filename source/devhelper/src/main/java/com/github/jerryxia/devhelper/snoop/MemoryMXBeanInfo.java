package com.github.jerryxia.devhelper.snoop;

import java.lang.management.MemoryUsage;

/**
 * @author Administrator
 *
 */
public class MemoryMXBeanInfo {

    private MemoryUsage heapMemoryUsag;
    private MemoryUsage nonHeapMemoryUsag;

    public MemoryUsage getHeapMemoryUsag() {
        return heapMemoryUsag;
    }

    public void setHeapMemoryUsag(MemoryUsage heapMemoryUsag) {
        this.heapMemoryUsag = heapMemoryUsag;
    }

    public MemoryUsage getNonHeapMemoryUsag() {
        return nonHeapMemoryUsag;
    }

    public void setNonHeapMemoryUsag(MemoryUsage nonHeapMemoryUsag) {
        this.nonHeapMemoryUsag = nonHeapMemoryUsag;
    }

}
