package org.anonymous.devhelper.snoop;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class JvmMemoryInfo {

    private MemoryMXBeanInfo           memoryMXBeanInfo;
    private List<MemoryPoolMXBeanInfo> memoryPoolMXBeansInfo;

    public MemoryMXBeanInfo getMemoryMXBeanInfo() {
        return memoryMXBeanInfo;
    }

    public void setMemoryMXBeanInfo(MemoryMXBeanInfo memoryMXBeanInfo) {
        this.memoryMXBeanInfo = memoryMXBeanInfo;
    }

    public List<MemoryPoolMXBeanInfo> getMemoryPoolMXBeansInfo() {
        return memoryPoolMXBeansInfo;
    }

    public void setMemoryPoolMXBeansInfo(List<MemoryPoolMXBeanInfo> memoryPoolMXBeansInfo) {
        this.memoryPoolMXBeansInfo = memoryPoolMXBeansInfo;
    }

}
