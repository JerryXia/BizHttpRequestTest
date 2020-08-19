/**
 * 
 */
package com.github.jerryxia.devhelper.support.spring.scheduling;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledAnnotationPointcutAdvisorFactory {

    public static DefaultPointcutAdvisor getScheduledAdvisor() {
        AnnotationMatchingPointcut pointcut = AnnotationMatchingPointcut.forMethodAnnotation(Scheduled.class);
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, null);
        return advisor;
    }
}
