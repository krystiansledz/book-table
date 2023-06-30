package com.krystiansledz.booktable.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ErrorLoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.krystiansledz.booktable..*(..))")
    public void publicMethod() {
    }

    @AfterThrowing(pointcut = "publicMethod()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in {}() with cause = {}", joinPoint.getSignature().getName(), exception.getCause() != null ? exception.getCause() : (exception.getMessage() != null ? exception.getMessage() : (exception.toString() != null ? exception.toString() : "NULL")));
    }
}
