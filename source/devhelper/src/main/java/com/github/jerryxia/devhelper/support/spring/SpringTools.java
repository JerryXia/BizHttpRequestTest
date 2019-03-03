/**
 * 
 */
package com.github.jerryxia.devhelper.support.spring;

import org.springframework.context.ApplicationContext;

/**
 * @author guqk
 *
 */
public class SpringTools {
    private static ApplicationContext applicationContext;

    static {
        // org.springframework.web.context.WebApplicationContext
        // applicationContext = ContextLoader.getCurrentWebApplicationContext();
    }

    public static void setApplicationContext(ApplicationContext context) {
        if (applicationContext == null) {
            applicationContext = context;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
