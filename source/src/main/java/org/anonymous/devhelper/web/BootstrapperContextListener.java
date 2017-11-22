/**
 * 
 */
package org.anonymous.devhelper.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.anonymous.devhelper.Bootstrapper;

/**
 * @author Administrator
 *
 */
public class BootstrapperContextListener implements ServletContextListener {

    private Bootstrapper bootstrapper;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        bootstrapper = new Bootstrapper();
        bootstrapper.javaMelodyChineseFontExtract();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (this.bootstrapper != null) {
            this.bootstrapper = null;
        }
    }


}