package com.guqiankun.devhelper.snoop;

import java.lang.management.MemoryType;
import java.lang.management.MemoryUsage;

public class MemoryPoolMXBeanInfo {

    private String    name;
    public MemoryType type;

    // Memory Usage Monitoring
    private MemoryUsage memoryUsage;
    private MemoryUsage peakMemoryUsage;
    private MemoryUsage collectionUsage;
    private long        usageThreshold;
    private long        collectionUsageThreshold;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MemoryType getType() {
        return type;
    }

    public void setType(MemoryType type) {
        this.type = type;
    }

    public MemoryUsage getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(MemoryUsage memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public MemoryUsage getPeakMemoryUsage() {
        return peakMemoryUsage;
    }

    public void setPeakMemoryUsage(MemoryUsage peakMemoryUsage) {
        this.peakMemoryUsage = peakMemoryUsage;
    }

    public MemoryUsage getCollectionUsage() {
        return collectionUsage;
    }

    public void setCollectionUsage(MemoryUsage collectionUsage) {
        this.collectionUsage = collectionUsage;
    }

    public long getUsageThreshold() {
        return usageThreshold;
    }

    public void setUsageThreshold(long usageThreshold) {
        this.usageThreshold = usageThreshold;
    }

    public long getCollectionUsageThreshold() {
        return collectionUsageThreshold;
    }

    public void setCollectionUsageThreshold(long collectionUsageThreshold) {
        this.collectionUsageThreshold = collectionUsageThreshold;
    }

}
