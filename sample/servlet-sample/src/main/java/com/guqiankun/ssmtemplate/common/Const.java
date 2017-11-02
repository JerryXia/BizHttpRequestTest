package com.guqiankun.ssmtemplate.common;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class Const {

    private static final Logger logger = LoggerFactory.getLogger(Const.class);

    public static Properties AppProperties = null;

    static {
        try {
            AppProperties = PropertiesLoaderUtils.loadAllProperties("app.properties");
        } catch (IOException e) {
            logger.error("load app.properties error", e);
        }
    }

}
