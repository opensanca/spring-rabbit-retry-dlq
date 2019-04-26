package io.github.opensanca.rabbit.listeners;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.opensanca.rabbit.aop.EnableRabbitRetryAndDlq;
import io.github.opensanca.rabbit.components.QueueRetryComponent;

@Component
public class TestListener {

    @Autowired private QueueRetryComponent queueRetryComponent;

    @RabbitListener(queues = "${listener.create-permission.queue}")
    @EnableRabbitRetryAndDlq(nameOfProperty = "createPermissionProperties")
    public void onMessage(Message message) {
        String messageStr = new String(message.getBody());

        if (messageStr.equals("dlq")) {
            throw new RuntimeException("to dead-letter");
        }

        System.out.println("message = [" + messageStr + "]");
    }

}
