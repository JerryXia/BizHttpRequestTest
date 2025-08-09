/**
 *
 */
package com.github.jerryxia.devhelperspringbootsample.task;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import com.github.jerryxia.devutil.SystemClock;
import com.vip.vjtools.vjkit.concurrent.ThreadUtil;
import com.vip.vjtools.vjkit.number.RandomUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CronTest {

    @Scheduled(cron = "0/10 * * * * ?")
    public void jobOne() {
        log.info("jobOne: start");
        ThreadUtil.sleep(RandomUtil.nextLong(1, 5) * 1000);

        int i = 1 / 0;
        log.info("result: {}", i);
    }

    @Schedules(value = {
            @Scheduled(cron = "0/7 * * * * ?")
    })
    public void jobTwo() {
        ThreadUtil.sleep(RandomUtil.nextLong(5, 10) * 1000);

        Date now = SystemClock.nowDate();
        if (now.getTime() % 2 == 0) {
            log.warn("random warn message, {}", now);
        } else {
            log.error("random error message, {}", now);
        }
    }
}
