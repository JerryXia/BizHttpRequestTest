/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.jerryxia.devhelper.requestcapture.support.servlet.RequestCaptureFilter;
import com.github.jerryxia.devhelper.requestcapture.support.servlet.RequestCaptureWebServlet;
import com.github.jerryxia.devhelper.web.filter.RequestIdInitFilter;

/**
 * @author guqk
 *
 */
@Configuration
@EnableConfigurationProperties(DevHelperProperties.class)
@ConditionalOnWebApplication
public class DevHelperAutoConfiguration {
    public static final String REQUEST_ID_INIT_FILTER_REGISTRATION_BEAN_NAME = "devhelper-requestIdInitFilter-registration";
    public static final String REQUEST_CAPTURE_FILTER_REGISTRATION_BEAN_NAME = "devhelper-requestCaptureFilter-registration";
    public static final String REQUEST_CAPTURE_WEB_SERVLET_REGISTRATION_BEAN_NAME = "devhelper-requestCaptureWebServlet-registration";

    @Bean(name = REQUEST_ID_INIT_FILTER_REGISTRATION_BEAN_NAME)
    @ConditionalOnMissingBean(name = REQUEST_ID_INIT_FILTER_REGISTRATION_BEAN_NAME)
    public FilterRegistrationBean requestIdInitFilter(DevHelperProperties properties, ServletContext servletContext) {
        String filterName = "requestIdInitFilter";
        RequestIdInitFilterProperties config = properties.getRequestIdInit();

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        RequestIdInitFilter filter = new RequestIdInitFilter();
        registrationBean.setFilter(filter);
        registrationBean.setName(filterName);
        registrationBean.addInitParameter(RequestIdInitFilter.PARAM_NAME_REQUEST_ID_RESPONSE_HEADER_NAME,
                config.getRequestIdResponseHeaderName());

        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);

        FilterRegistration filterRegistration = servletContext.getFilterRegistration(filterName);
        if (filterRegistration != null) {
            // if webapp deployed as war in a container with MonitoringFilter already added by web-fragment.xml,
            // do not try to add it again
            registrationBean.setEnabled(false);
            for (Map.Entry<String, String> entry : registrationBean.getInitParameters().entrySet()) {
                filterRegistration.setInitParameter(entry.getKey(), entry.getValue());
            }
        }
        return registrationBean;
    }

    @Bean(name = REQUEST_CAPTURE_FILTER_REGISTRATION_BEAN_NAME)
    @ConditionalOnMissingBean(name = REQUEST_CAPTURE_FILTER_REGISTRATION_BEAN_NAME)
    public FilterRegistrationBean requestCaptureFilter(DevHelperProperties properties, ServletContext servletContext) {
        String filterName = "requestCaptureFilter";
        RequestCaptureFilterProperties config = properties.getRequestCapture();

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        RequestCaptureFilter filter = new RequestCaptureFilter();
        registrationBean.setFilter(filter);
        registrationBean.setName(filterName);
        registrationBean.addInitParameter(RequestCaptureFilter.PARAM_NAME_EXCLUSIONS, config.getExclusions());
        registrationBean.addInitParameter(RequestCaptureFilter.PARAM_NAME_REPLAY_REQUEST_ID_REQUEST_HEADER_NAME,
                config.getReplayRequestIdRequestHeaderName());

        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);

        FilterRegistration filterRegistration = servletContext.getFilterRegistration(filterName);
        if (filterRegistration != null) {
            // if webapp deployed as war in a container with MonitoringFilter already added by web-fragment.xml,
            // do not try to add it again
            registrationBean.setEnabled(false);
            for (Map.Entry<String, String> entry : registrationBean.getInitParameters().entrySet()) {
                filterRegistration.setInitParameter(entry.getKey(), entry.getValue());
            }
        }
        return registrationBean;
    }


    @Bean(name = REQUEST_CAPTURE_WEB_SERVLET_REGISTRATION_BEAN_NAME)
    @ConditionalOnMissingBean(name = REQUEST_CAPTURE_WEB_SERVLET_REGISTRATION_BEAN_NAME)
    public ServletRegistrationBean monitoringSessionListener(DevHelperProperties properties, ServletContext servletContext) {
        String servletName = "requestCaptureWebServlet";
        RequestCaptureWebServletProperties config = properties.getRequestCaptureServlet();

        ServletRegistrationBean registrationBean = new ServletRegistrationBean();

        RequestCaptureWebServlet servlet = new RequestCaptureWebServlet();
        registrationBean.setServlet(servlet);
        registrationBean.setName(servletName);

        if(config.getUrlPattern() != null) {
            registrationBean.addUrlMappings(config.getUrlPattern());
        } else {
            registrationBean.addUrlMappings("/requestcapture/*");
        }

        ServletRegistration servletRegistration = servletContext.getServletRegistration(servletName);
        if (servletRegistration != null) {
            // if webapp deployed as war in a container with MonitoringFilter and SessionListener already added by web-fragment.xml,
            // do not add again
            registrationBean.setEnabled(false);
            for (Map.Entry<String, String> entry : registrationBean.getInitParameters().entrySet()) {
                servletRegistration.setInitParameter(entry.getKey(), entry.getValue());
            }
        }
        return registrationBean;
    }
}