package com.example.telproject.config;

import com.example.telproject.TelProjectApplication;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {

    Logger logger = LoggerFactory.getLogger(TelProjectApplication.class);

    @Pointcut("execution(public * com.example.telproject.controller.*.*(..))")
    public void methodExecuting() {
    }

    /**
     * This method is executed after the successful execution of any method annotated with the custom @methodExecuting annotation.
     * It records the successful execution of the method, along with its return value (if any) and the class it belongs to,
     * in the log file.
     *
     * @param joinPoint      the join point representing the point at which the method was executed
     * @param returningValue the return value of the method
     */
    @AfterReturning(value = "methodExecuting()",
            returning = "returningValue")
    public void recordSuccessfulExecution(JoinPoint joinPoint,
                                          Object returningValue) {
        String info;
        if (returningValue != null) {
            info = String.format("\nMethod %s, from Class %s, successfully called with return value: %s\n",
                    joinPoint.getSignature().getName(),
                    joinPoint.getSourceLocation().getWithinType().getName(),
                    returningValue
            );
        } else {
            info = String.format("\nMethod %s, from Class %s, successfully called\n",
                    joinPoint.getSignature().getName(),
                    joinPoint.getSourceLocation().getWithinType().getName());
        }
        logger.info(info);
    }

    /**
     * An aspect that logs information about the failed execution of a method.
     * This method is executed when any exception is thrown from a method with the pointcut expression "methodExecuting()".
     * The method logs information about the failed execution of the method, including the method name, the class name, and the exception message.
     *
     * @param joinPoint the join point at which the exception was thrown
     */
    @AfterThrowing(value = "methodExecuting()",
            throwing = "exception")
    public void recordFailedExecution(JoinPoint joinPoint, Exception exception) {
        String error = String.format("\nMethod %s, from Class %s, throw an error: %s\n",
                joinPoint.getSignature().getName(),
                joinPoint.getClass().getName(),
                exception);
        logger.error(error);
    }
}
