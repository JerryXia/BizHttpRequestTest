/**
 * 
 */
package com.github.jerryxia.devhelper.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.github.jerryxia.devhelper.Bootstrapper;

/**
 * @author Administrator
 *
 */
public final class BootstrapperContextListener implements ServletContextListener {
    private Bootstrapper bootstrapper;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        bootstrapper = new Bootstrapper();
        bootstrapper.init();
        event.getServletContext().log("devhelper BootstrapperContextListener contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (this.bootstrapper != null) {
            this.bootstrapper = null;
        }
        event.getServletContext().log("devhelper BootstrapperContextListener contextDestroyed");
    }

}