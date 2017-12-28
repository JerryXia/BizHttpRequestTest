/**
 * 
 */
package com.github.jerryxia.devhelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 引导器
 * 
 * @author guqk
 */
public class Bootstrapper {
    private String      javaHome = null;
    private ClassLoader clToUse  = null;

    /**
     * ctors
     */
    public Bootstrapper() {
        javaHome = System.getProperty("java.home");
        clToUse = Bootstrapper.class.getClassLoader();
    }

    public void init() {
        this.javaMelodyChineseFontExtract();
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
                String fontFilePath = javaHome + "/lib/fonts/fallback/" + fontFileName;
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
