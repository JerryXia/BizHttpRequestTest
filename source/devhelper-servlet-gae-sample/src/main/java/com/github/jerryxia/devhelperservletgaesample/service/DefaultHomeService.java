package com.github.jerryxia.devhelperservletgaesample.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.github.jerryxia.devhelperservletgaesample.common.Const;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DefaultHomeService {

    // @Cacheable("time")
    public String query() {
        String appName = Const.AppProperties.getProperty("app.name");
        log.debug("appName: {}, now: {}", appName, DateTime.now(DateTimeZone.UTC).toString("yyyy-MM-dd HH:mm:ss"));
        return appName;
    }

}