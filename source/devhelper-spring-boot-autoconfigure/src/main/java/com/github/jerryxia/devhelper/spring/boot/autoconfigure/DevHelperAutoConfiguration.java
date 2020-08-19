/**
 * 
 */
package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.jerryxia.devhelper.elmah.support.servlet.ElmahServlet;
import com.github.jerryxia.devhelper.requestcapture.support.servlet.RequestCaptureFilter;
import com.github.jerryxia.devhelper.support.spring.scheduling.ScheduledAnnotationPointcutAdvisorFactory;
import com.github.jerryxia.devhelper.support.spring.scheduling.ScheduledTaskRunRecordAutoInjectInterceptor;
import com.github.jerryxia.devhelper.support.spring.web.interceptor.RequestResponseLogInterceptor;
import com.github.jerryxia.devhelper.support.web.filter.RequestIdInitFilter;
import com.github.jerryxia.devhelper.support.web.listener.BootstrapperContextListener;
import com.github.jerryxia.devhelper.support.web.servlet.DispatchWebRequestServlet;

/**
 * @author guqk
 *
 */
@Configuration
@EnableConfigurationProperties(DevHelperProperties.class)
@ConditionalOnWebApplication
public class DevHelperAutoConfiguration extends WebMvcConfigurerAdapter {
    public static final String REQUEST_ID_INIT_FILTER_REGISTRATION_BEAN_NAME       = "devhelper-requestIdInitFilter-registration";
    public static final String REQUEST_CAPTURE_FILTER_REGISTRATION_BEAN_NAME       = "devhelper-requestCaptureFilter-registration";
    public static final String DISPATCH_WEB_REQUEST_SERVLET_REGISTRATION_BEAN_NAME = "devhelper-dispatchWebRequestServlet-registration";
    public static final String ELMAH_WEB_SERVLET_REGISTRATION_BEAN_NAME            = "devhelper-elmahWebServlet-registration";

    @Autowired
    private DevHelperProperties properties;
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ServletListenerRegistrationBean<BootstrapperContextListener> bootstrapperContextListener() {
        String listenerName = "bootstrapperContextListener";

        ServletListenerRegistrationBean<BootstrapperContextListener> registrationBean = new ServletListenerRegistrationBean<BootstrapperContextListener>();

        BootstrapperContextListener listener = new BootstrapperContextListener();
        registrationBean.setListener(listener);
        registrationBean.setName(listenerName);

        return registrationBean;
    }

    @Bean(name = REQUEST_ID_INIT_FILTER_REGISTRATION_BEAN_NAME)
    @ConditionalOnMissingBean(name = REQUEST_ID_INIT_FILTER_REGISTRATION_BEAN_NAME)
    public FilterRegistrationBean requestIdInitFilter(DevHelperProperties properties, ServletContext servletContext) {
        String filterName = "requestIdInitFilter";
        RequestIdInitFilterProperties config = properties.getRequestIdInit();

        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        RequestIdInitFilter filter = new RequestIdInitFilter();
        registrationBean.setFilter(filter);
        registrationBean.setName(filterName);
        // 优先使用此filter
        registrationBean.setOrder(FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER);
        if (config.getRequestIdResponseHeaderName() != null) {
            registrationBean.addInitParameter(RequestIdInitFilter.PARAM_NAME_REQUEST_ID_RESPONSE_HEADER_NAME, config.getRequestIdResponseHeaderName());
        }

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
        // 优先使用此filter
        registrationBean.setOrder(FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER);
        if (config.getEnabled() != null) {
            registrationBean.addInitParameter(RequestCaptureFilter.PARAM_NAME_ENABLED, config.getEnabled().toString());
        }
        if (config.getExclusions() != null) {
            registrationBean.addInitParameter(RequestCaptureFilter.PARAM_NAME_EXCLUSIONS, config.getExclusions());
        }
        if (config.getReplayRequestIdRequestHeaderName() != null) {
            registrationBean.addInitParameter(RequestCaptureFilter.PARAM_NAME_REPLAY_REQUEST_ID_REQUEST_HEADER_NAME, config.getReplayRequestIdRequestHeaderName());
        }
        if (config.getMaxPayloadLength() != null) {
            registrationBean.addInitParameter(RequestCaptureFilter.PARAM_NAME_MAX_PAYLOAD_LENGTH, config.getMaxPayloadLength().toString());
        }

        registrationBean.addUrlPatterns("/*");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR);

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

    @Bean(name = DISPATCH_WEB_REQUEST_SERVLET_REGISTRATION_BEAN_NAME)
    @ConditionalOnMissingBean(name = DISPATCH_WEB_REQUEST_SERVLET_REGISTRATION_BEAN_NAME)
    public ServletRegistrationBean dispatchWebRequestServlet(DevHelperProperties properties, ServletContext servletContext) {
        String servletName = "dispatchWebServlet";
        RequestCaptureWebServletProperties config = properties.getRequestCaptureServlet();

        ServletRegistrationBean registrationBean = new ServletRegistrationBean();

        DispatchWebRequestServlet servlet = new DispatchWebRequestServlet();
        registrationBean.setServlet(servlet);
        registrationBean.setName(servletName);
        registrationBean.setLoadOnStartup(1);

        if (config.getUrlPattern() != null) {
            registrationBean.addUrlMappings(config.getUrlPattern());
        } else {
            registrationBean.addUrlMappings("/admin/requestcapture/*");
        }

        ServletRegistration servletRegistration = servletContext.getServletRegistration(servletName);
        if (servletRegistration != null) {
            // if webapp deployed as war in a container with MonitoringFilter and SessionListener already added by
            // web-fragment.xml,
            // do not add again
            registrationBean.setEnabled(false);
            for (Map.Entry<String, String> entry : registrationBean.getInitParameters().entrySet()) {
                servletRegistration.setInitParameter(entry.getKey(), entry.getValue());
            }
        }
        return registrationBean;
    }

    @Bean(name = ELMAH_WEB_SERVLET_REGISTRATION_BEAN_NAME)
    @ConditionalOnMissingBean(name = ELMAH_WEB_SERVLET_REGISTRATION_BEAN_NAME)
    public ServletRegistrationBean elmahWebServlet(DevHelperProperties properties, ServletContext servletContext) {
        String servletName = "elmahWebServlet";
        ElmahServletProperties config = properties.getElmahServlet();

        ServletRegistrationBean registrationBean = new ServletRegistrationBean();

        ElmahServlet servlet = new ElmahServlet();
        registrationBean.setServlet(servlet);
        registrationBean.setName(servletName);
        registrationBean.setLoadOnStartup(1);

        if (config.getUrlPattern() != null) {
            registrationBean.addUrlMappings(config.getUrlPattern());
        } else {
            registrationBean.addUrlMappings("/admin/elmah/*");
        }

        if (config.getErrorRecordStorage() != null) {
            registrationBean.addInitParameter(ElmahServlet.PARAM_NAME_ERROR_RECORD_STORAGE, config.getErrorRecordStorage());
        }
        if (config.getErrorRecordFileStoragePath() != null) {
            registrationBean.addInitParameter(ElmahServlet.PARAM_NAME_ERROR_RECORD_FILE_STORAGE_PATH, config.getErrorRecordFileStoragePath());
        }

        ServletRegistration servletRegistration = servletContext.getServletRegistration(servletName);
        if (servletRegistration != null) {
            // if webapp deployed as war in a container with MonitoringFilter and SessionListener already added by
            // web-fragment.xml,
            // do not add again
            registrationBean.setEnabled(false);
            for (Map.Entry<String, String> entry : registrationBean.getInitParameters().entrySet()) {
                servletRegistration.setInitParameter(entry.getKey(), entry.getValue());
            }
        }
        return registrationBean;
    }

    @Bean(initMethod = "init")
    public RequestResponseLogInterceptor requestResponseLogInterceptor() {
        RequestResponseLogProperties config = properties.getRequestResponseLog();
        RequestResponseLogInterceptor interceptor = new RequestResponseLogInterceptor();
        if (config.getEnabled() != null) {
            interceptor.setEnabled(config.getEnabled().booleanValue());
        }
        if (!StringUtils.isEmpty(config.getLogRequestHeaderNames())) {
            String[] toLogReqHeadNames = config.getLogRequestHeaderNames().split(",");
            ArrayList<String> list = new ArrayList<String>();
            for (String reqHeadName : toLogReqHeadNames) {
                String trimReqHeadName = reqHeadName.trim();
                if (!StringUtils.isEmpty(trimReqHeadName)) {
                    list.add(trimReqHeadName);
                }
            }
            interceptor.setLogRequestHeaderNames(list.toArray(new String[list.size()]));
        }
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(requestResponseLogInterceptor()).addPathPatterns("/**");
    }

    @Bean
    @ConditionalOnClass(Scheduled.class)
    public ScheduledTaskRunRecordAutoInjectInterceptor scheduledTaskRunRecordAutoInjectInterceptor() {
        TaskRunProperties config = properties.getTaskRun();
        ScheduledTaskRunRecordAutoInjectInterceptor interceptpr = new ScheduledTaskRunRecordAutoInjectInterceptor();
        if (config.getLazyMode() != null) {
            interceptpr.setLazyMode(config.getLazyMode().booleanValue());
        }
        interceptpr.setInstanceName(applicationContext.getId());
        return interceptpr;
    }

    @Bean
    public DefaultPointcutAdvisor scheduledAdvisor() {
        DefaultPointcutAdvisor scheduledAdvisor = ScheduledAnnotationPointcutAdvisorFactory.getScheduledAdvisor();
        scheduledAdvisor.setAdvice(scheduledTaskRunRecordAutoInjectInterceptor());
        return scheduledAdvisor;
    }

}
