package io.github.opensanca.rabbit.components;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.opensanca.rabbit.config.properties.generic.QueueProperties;

@Component
public class QueueRetryComponent {

    private static final String X_DEATH = "x-death";

    private final RabbitTemplate rabbitTemplate;

    private static Logger log = LoggerFactory.getLogger(QueueRetryComponent.class);

    @Autowired
    public QueueRetryComponent(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToRetryOrDlq(final Message message, final QueueProperties properties) {
        final Integer qtyRetry = countDeath(message);

        if (qtyRetry > properties.getMaxRetriesAttempts()) {
            sendToDlq(message, properties);
        } else {
            sendToRetry(message, properties);
        }
    }

    private void sendToRetry(final Message message, final QueueProperties properties) {
        message.getMessageProperties().setExpiration(properties.getTtlRetryMessage());
        rabbitTemplate.send(properties.getExchange(), properties.getQueueRetry(), message);
        log.info("M=sendToRetry, Message={}", message);
    }

    private int countDeath(final Message message) {
        int count = 0;
        final Map<String, Object> headers = message.getMessageProperties().getHeaders();
        if (headers.containsKey(X_DEATH)) {
            count = Integer.parseInt(getXDeath(headers).get("count").toString());
        }
        return ++count;
    }

    private Map getXDeath(final Map<String, Object> headers) {
        final List list = (List) Collections.singletonList(headers.get(X_DEATH)).get(0);
        return (Map) list.get(0);
    }

    private void sendToDlq(final Message message, final QueueProperties properties) {
        message.getMessageProperties().getHeaders().remove(X_DEATH);
        rabbitTemplate.send(properties.getExchange(), properties.getQueueDlq(), message);
        log.info("M=sendToDlq, Message={}", message);
    }
}
