/**
 * 
 */
package com.github.jerryxia.devhelper.web.servlet;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.github.jerryxia.devhelper.web.listener.BootstrapperContextListener;

/**
 * @author Administrator
 *
 */
public class BootstrapperServletContainerInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
        BootstrapperContextListener listener = new BootstrapperContextListener();
        try {
            ctx.addListener(listener);
        } catch (RuntimeException ex) {
            throw new IllegalStateException("Failed to add listener '" + listener + "' to servlet context", ex);
        }
    }
}