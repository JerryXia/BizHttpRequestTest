/**
 * 
 */
package com.github.jerryxia.devhelper.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.github.jerryxia.devhelper.Bootstrapper;
import com.github.jerryxia.devhelper.web.WebConstants;

/**
 * @author Administrator
 *
 */
public final class BootstrapperContextListener implements ServletContextListener {

    private Bootstrapper bootstrapper;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        long startTimeStamp = WebConstants.START_TIMESTAMP;
        if (startTimeStamp > 0) {
            bootstrapper = new Bootstrapper();
            bootstrapper.javaMelodyChineseFontExtract();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        if (this.bootstrapper != null) {
            this.bootstrapper = null;
        }
    }

}