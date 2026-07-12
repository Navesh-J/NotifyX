package com.navesh.notifyx.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class NotificationLoggingAspect {

    @Pointcut("""
            execution(public * com.navesh.notifyx.service..*(..))
            || execution(public * com.navesh.notifyx.impl..*(..))
            || execution(public * com.navesh.notifyx.gateway..*(..))
            """)
    public void applicationLayer() {
    }

    @Around("applicationLayer()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.nanoTime();

        String className = joinPoint.getSignature()
                .getDeclaringType()
                .getSimpleName();

        String methodName = joinPoint.getSignature()
                .getName();

        log.info("Entering {}.{}", className, methodName);

        try {

            Object result = joinPoint.proceed();

            long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            log.info(
                    "Completed {}.{} in {} ms",
                    className,
                    methodName,
                    elapsedMs
            );

            return result;

        } catch (Throwable ex) {

            long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            log.error(
                    "Exception in {}.{} after {} ms",
                    className,
                    methodName,
                    elapsedMs,
                    ex
            );

            throw ex;
        }
    }
}
