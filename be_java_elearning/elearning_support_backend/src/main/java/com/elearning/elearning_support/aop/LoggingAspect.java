package com.elearning.elearning_support.aop;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j(topic = "AoP-LoggingAspect")
public class LoggingAspect {

    @Around(value = "@annotation(com.elearning.elearning_support.annotations.log.ExecutionTimeLog)")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final String methodName = proceedingJoinPoint.getSignature().getName();
        final String classSimpleName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        // log
        long startLoggedTimeInMillis = System.currentTimeMillis();
        log.info("===== STARTED METHOD [{}] IN [{}] =====", methodName, classSimpleName);
        Object proceed = proceedingJoinPoint.proceed();
        log.info("===== ENDED METHOD [{}] IN [{}] AFTER {} ms =====", methodName, classSimpleName, System.currentTimeMillis() - startLoggedTimeInMillis);
        return proceed;
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "com.elearning.elearning_support.aop.AspectPointcut.applicationPackagePointcut() " +
        "&& com.elearning.elearning_support.aop.AspectPointcut.applicationPackagePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("THROWN: Exception in {}.{} ", joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(), e);
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws Exception
     */
    @Around("com.elearning.elearning_support.aop.AspectPointcut.applicationPackagePointcut() " +
        "&& com.elearning.elearning_support.aop.AspectPointcut.applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("ENTERED: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                Arrays.stream(Optional.ofNullable(joinPoint.getArgs()).orElse(ArrayUtils.EMPTY_OBJECT_ARRAY))
                    .map(x -> Objects.isNull(x) ? null : ToStringBuilder.reflectionToString(x)).collect(Collectors.toList()));
            Object result = joinPoint.proceed();
            log.info("EXITED: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), result);
            return result;
        } catch (Exception e) {
            log.error("THROWN: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
