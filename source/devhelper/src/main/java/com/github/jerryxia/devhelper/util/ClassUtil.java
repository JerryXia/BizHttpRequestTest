/**
 * 
 */
package com.github.jerryxia.devhelper.util;

/**
 * @author Administrator
 *
 */
public class ClassUtil {
    private static final ClassLoader defaultClassLoader = ClassUtil.class.getClassLoader();

    public static ClassLoader getClassLoader() {
        if (defaultClassLoader != null) {
            return defaultClassLoader;
        } else {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    /**
     * Loads a class
     * 
     * @param className
     *            - the class to load
     * @return The loaded class
     * @throws ClassNotFoundException
     *             If the class cannot be found (duh!)
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        Class<?> clazz = null;
        try {
            clazz = getClassLoader().loadClass(className);
        } catch (Exception e) {
            // Ignore. Failsafe below.
        }
        if (clazz == null) {
            clazz = Class.forName(className);
        }
        return clazz;
    }
}
