package com.github.jerryxia.devhelper.spring.boot.autoconfigure;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.github.jerryxia.devhelper.support.log.LogbackAppender;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/**
 * @author guqk
 * @date 2021/08/04
 */
@Configuration
@ConditionalOnClass(LoggerContext.class)
@ConditionalOnProperty(name = "devhelper.request-capture-servlet.enabled", havingValue = "true")
public class DevHelperLogbackLoggingAutoConfiguration {
    private static final String DEFAULT_APPENDER_NAME = "DEVHELPER";

    @PostConstruct
    public void init() {
        ILoggerFactory factory = LoggerFactory.getILoggerFactory();
        Assert.isInstanceOf(LoggerContext.class, factory, "LoggerFactory is not a Logback LoggerContext");
        LoggerContext loggerContext = (LoggerContext) factory;

        LogbackAppender appender = new LogbackAppender();
        appender.setName(DEFAULT_APPENDER_NAME);
        appender.start();

        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(appender);
    }
}