package com.guqiankun.ssmtemplate.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoBizTask {

    private final Logger logger = LoggerFactory.getLogger(DemoBizTask.class);

    @Scheduled(cron = "0  0/1  9-17  *  *  ? ")
    public void addUserScore() {
        logger.info("@Scheduled--------addUserScore()");
    }

    @Scheduled(cron = "0/40  *  *  *  *  ? ")
    public void cacheClear() {

            logger.info("@Scheduled-------cacheClear()");

    }
}
