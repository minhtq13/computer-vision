package com.elearning.elearning_support.aop;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class AspectPointcut {

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @org.aspectj.lang.annotation.Pointcut("within(@org.springframework.stereotype.Repository *)" +
        " || within(@org.springframework.stereotype.Service *)" +
        " || within(@org.springframework.web.bind.annotation.RestController *)" +
        " || within(@org.springframework.stereotype.Controller *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Pointcut that matches all Spring beans in the application's main packages.
     */
    @org.aspectj.lang.annotation.Pointcut("within(com.elearning.elearning_support.services..*)" +
        " || within(com.elearning.elearning_support.controllers..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }


    /**
     * Point cut for all methods in the class has @RestController annotation
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)" +
        "|| within(@org.springframework.stereotype.Controller *)")
    public void springControllerPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

}
