/**
 * 
 */
package org.anonymous.devhelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author guqk
 *
 */
public class Bootstrapper {
    private static String javaHome = null;

    static {
        javaHome = System.getProperty("java.home");
    }

    private static void javameloadChineseFontInit() {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("simsun.ttc");
        FileOutputStream fos = null;
        try {
            String des = javaHome + "/lib/fonts/fallback/simsun.ttc";
            fos = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
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
