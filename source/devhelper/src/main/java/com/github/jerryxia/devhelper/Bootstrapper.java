/**
 * 
 */
package com.github.jerryxia.devhelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 引导器
 * 
 * @author guqk
 */
public class Bootstrapper {
    private ClassLoader clToUse = null;

    /**
     * ctors
     */
    public Bootstrapper() {
        clToUse = Bootstrapper.class.getClassLoader();
    }

    public void init() {
        this.getCurrentLibVersion();
        this.javaMelodyChineseFontExtract();
    }

    private void getCurrentLibVersion() {
        Constants.START_TIME = System.currentTimeMillis();
        // Constants.START_TIME = ManagementFactory.getRuntimeMXBean().getStartTime();
        Constants.SERVER_OS_NAME = System.getProperty("os.name");
        Constants.JAVA_VM_NAME = System.getProperty("java.vm.name");
        Constants.JAVA_VERSION = System.getProperty("java.version");
        Constants.JAVA_HOME = System.getProperty("java.home");
        Constants.JAVA_CLASS_PATH = System.getProperty("java.class.path");

        Properties prop = new Properties();
        try {
            prop.load(this.clToUse.getResourceAsStream("META-INF/maven/com.github.jerryxia/devhelper/pom.properties"));
            Constants.VERSION = prop.getProperty("version");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解压宋体的字体文件到 {java.home}/lib/fonts/fallback/
     */
    private void javaMelodyChineseFontExtract() {
        String javaMelodyMonitoringFilterClassName = "net.bull.javamelody.MonitoringFilter";
        boolean existsJavaMelodyClass = classforName(javaMelodyMonitoringFilterClassName) != null;
        if (existsJavaMelodyClass) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                // 先检查字体文件目录
                String fontFileName = "simsun.ttc";
                String fontFilePath = Constants.JAVA_HOME + "/lib/fonts/fallback/" + fontFileName;
                File fontFile = new File(fontFilePath);
                if (!fontFile.getParentFile().exists()) {
                    if (fontFile.getParentFile().mkdirs() == false) {
                        return;
                    }
                }
                // 释放字体文件
                is = clToUse.getResourceAsStream(fontFileName);
                fos = new FileOutputStream(fontFilePath);
                byte[] buffer = new byte[8192];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 判断类是否存在
     * 
     * @param className
     * @return
     */
    private Class<?> classforName(String className) {
        try {
            return clToUse != null ? clToUse.loadClass(className) : Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
