/**
 * 
 */
package com.github.jerryxia.devhelperspringbootsample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * from:
 * <p>
 * {@link org.springframework.scheduling.annotation.AbstractAsyncConfiguration} setConfigurers
 * </p>
 * <p>
 * {@link org.springframework.scheduling.annotation.ProxyAsyncConfiguration}
 * </p>
 * 
 * @author JerryXia
 *
 */
@EnableScheduling
@Configuration
public class ApplicationSchedulerConfiguration implements SchedulingConfigurer {
    private static final Logger log = LoggerFactory.getLogger(ApplicationSchedulerConfiguration.class);

    /**
     * for spring scheduling, beanName: taskScheduler
     * 
     * @return org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
     */
    @Bean(name = ScheduledAnnotationBeanPostProcessor.DEFAULT_TASK_SCHEDULER_BEAN_NAME, destroyMethod = "destroy")
    public ThreadPoolTaskScheduler taskScheduler() {
        if (log.isDebugEnabled()) {
            log.debug("Initializing ExecutorService 'taskScheduler' -> begin constructor");
        }
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(availableProcessors);
        scheduler.setThreadNamePrefix("task-scheduler-");
        // scheduler.setWaitForTasksToCompleteOnShutdown(true);
        // scheduler.setAwaitTerminationSeconds(4);
        return scheduler;
    }

    /**
     * for spring scheduling, ScheduledAnnotationBeanPostProcessor.finishRegistration
     * 
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (log.isDebugEnabled()) {
            log.debug("configure Tasks -> begin");
        }
        // if (taskRegistrar.getScheduler() == null) {
        // taskRegistrar.setScheduler(taskScheduler());
        // }
        if (log.isDebugEnabled()) {
            log.debug("configure Tasks -> end");
        }
    }
}
