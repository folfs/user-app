package com.demo.user_app.metrics;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class MetricsAspect {
    private static final Logger logger = LoggerFactory.getLogger(MetricsAspect.class);

    @Pointcut("execution(* com.demo.user_app.controller.*.*(..))")
    public void allMethodsInController() {}

    @Around("allMethodsInController()")
    public Object logRequestUriAndTime(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        long startTime = System.nanoTime();
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        logger.info("{} Request URI: {} | time taken: {} ms", method, uri, duration / 1_000_000.0);
        return result;
    }
}
