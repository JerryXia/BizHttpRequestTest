/**
 * 
 */
package com.github.jerryxia.devhelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author guqk
 *
 */
public class Bootstrapper {
    private String      javaHome = null;
    private ClassLoader clToUse  = null;

    public Bootstrapper() {
        javaHome = System.getProperty("java.home");
        clToUse = Bootstrapper.class.getClassLoader();
    }

    public void javaMelodyChineseFontExtract() {
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

    private Class<?> classforName(String className) {
        try {
            return clToUse != null ? clToUse.loadClass(className) : Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
