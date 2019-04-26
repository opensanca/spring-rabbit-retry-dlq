package io.github.opensanca.rabbit.aop;

import org.aspectj.lang.annotation.Pointcut;

public class CommonJoinPointConfig {

    @Pointcut("@annotation(io.github.opensanca.rabbit.aop.EnableRabbitRetryAndDlq)")
    public void enableRabbitRetryAndDlqAnnotation() {
    }

}
