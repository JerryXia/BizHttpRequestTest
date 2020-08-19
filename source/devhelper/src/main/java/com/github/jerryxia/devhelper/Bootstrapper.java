/**
 * 
 */
package com.github.jerryxia.devhelper;

import java.io.IOException;
import java.util.Properties;

import com.github.jerryxia.devhelper.util.ClassUtil;

/**
 ** 引导器
 * 
 * @author guqk
 */
public class Bootstrapper {
    private final ClassLoader clToUse;

    /**
     * ctors
     */
    public Bootstrapper() {
        this.clToUse = ClassUtil.getClassLoader();
    }

    public void init() {
        this.getCurrentLibVersion();
    }

    public void shutdown() {
        Constants.EVENT_WORKING_GROUP.shutdown();
    }

    private void getCurrentLibVersion() {
        Constants.START_TIME = System.currentTimeMillis();
        // Constants.START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();
        Constants.SERVER_OS_NAME = getSystemProperty("os.name");
        Constants.JAVA_VM_NAME = getSystemProperty("java.vm.name");
        Constants.JAVA_VERSION = getSystemProperty("java.version");
        Constants.JAVA_HOME = getSystemProperty("java.home");
        Constants.JAVA_CLASS_PATH = getSystemProperty("java.class.path");
        Constants.JAVA_IO_TMPDIR = getSystemProperty("java.io.tmpdir");

        Properties prop = new Properties();
        try {
            prop.load(this.clToUse.getResourceAsStream("META-INF/maven/com.github.jerryxia/devhelper/pom.properties"));
            Constants.VERSION = prop.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSystemProperty(String key) {
        String p = null;
        try {
            p = System.getProperty(key);
        } catch (SecurityException e) {
            p = "unknown";
        }
        return p;
    }

}
