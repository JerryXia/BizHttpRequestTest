/**
 * 
 */
package com.github.jerryxia.devhelper.support.spring.scheduling;

import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;

public class ScheduledAnnotationPointcutAdvisorFactory {

    public static DefaultPointcutAdvisor getScheduledAdvisor() {
        AnnotationMatchingPointcut scheduledPointcut = AnnotationMatchingPointcut.forMethodAnnotation(Scheduled.class);
        AnnotationMatchingPointcut schedulesPointcut = AnnotationMatchingPointcut.forMethodAnnotation(Schedules.class);
        ComposablePointcut pointcut = new ComposablePointcut(scheduledPointcut).union(schedulesPointcut);

        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, null);
        return advisor;
    }
}
