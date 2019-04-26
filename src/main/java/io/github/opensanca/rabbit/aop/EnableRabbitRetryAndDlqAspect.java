package io.github.opensanca.rabbit.aop;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import io.github.opensanca.rabbit.components.QueueRetryComponent;
import io.github.opensanca.rabbit.config.properties.generic.QueueProperties;

@Aspect
@Configuration
public class EnableRabbitRetryAndDlqAspect {

    private final QueueRetryComponent queueRetryComponent;
    private final Map<String, QueueProperties> queuesProperties;

    @Autowired
    public EnableRabbitRetryAndDlqAspect(QueueRetryComponent queueRetryComponent, Map<String, QueueProperties> queuesProperties) {
        this.queueRetryComponent = queueRetryComponent;
        this.queuesProperties = queuesProperties;
    }

    @Around("io.github.opensanca.rabbit.aop.CommonJoinPointConfig.enableRabbitRetryAndDlqAnnotation()")
    public void validateMessage(ProceedingJoinPoint joinPoint) throws Throwable {
        String queueProperty = getMethod(joinPoint).getAnnotation(EnableRabbitRetryAndDlq.class).nameOfProperty();
        Message message = (Message) joinPoint.getArgs()[0];
        try {
            joinPoint.proceed();
        } catch (Exception e) {
            QueueProperties properties = queuesProperties.get(queueProperty);
            if (Objects.isNull(properties)) {
                throw new NoSuchBeanDefinitionException(String.format("Any bean with name %s was found", queueProperty));
            }
            queueRetryComponent.sendToRetryOrDlq(message, properties);
        }
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
