package org.anonymous.devhelper.snoop;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author guqiankun
 *
 */
public class Monitor {

    private MemoryMXBean           memoryMXBean;
    private List<MemoryPoolMXBean> memoryPoolMXBeans;

    private Monitor() {
        memoryMXBean = ManagementFactory.getMemoryMXBean();
        memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
    }

    public JvmMemoryInfo run() {
        JvmMemoryInfo jvmMemoryInfo = new JvmMemoryInfo();
        // Memory MXBean
        MemoryMXBeanInfo memoryMXBeanInfo = new MemoryMXBeanInfo();
        memoryMXBeanInfo.setHeapMemoryUsag(memoryMXBean.getHeapMemoryUsage());
        memoryMXBeanInfo.setNonHeapMemoryUsag(memoryMXBean.getNonHeapMemoryUsage());
        jvmMemoryInfo.setMemoryMXBeanInfo(memoryMXBeanInfo);
        // Memory Pool MXBeans
        List<MemoryPoolMXBeanInfo> memoryPoolMXBeansInfo = new ArrayList<MemoryPoolMXBeanInfo>(
                memoryPoolMXBeans.size());
        for (MemoryPoolMXBean item : memoryPoolMXBeans) {
            MemoryPoolMXBeanInfo e = new MemoryPoolMXBeanInfo();
            e.setName(item.getName());
            e.setType(item.getType());
            e.setMemoryUsage(item.getUsage());
            e.setPeakMemoryUsage(item.getPeakUsage());
            e.setCollectionUsage(item.getCollectionUsage());
            e.setUsageThreshold(item.isUsageThresholdSupported() ? item.getUsageThreshold() : -1);
            e.setCollectionUsageThreshold(
                    item.isCollectionUsageThresholdSupported() ? item.getCollectionUsageThreshold() : -1);
            memoryPoolMXBeansInfo.add(e);
        }
        jvmMemoryInfo.setMemoryPoolMXBeansInfo(memoryPoolMXBeansInfo);
        return jvmMemoryInfo;
    }

    public static Monitor currentMonitor() {
        return new Monitor();
    }

}
