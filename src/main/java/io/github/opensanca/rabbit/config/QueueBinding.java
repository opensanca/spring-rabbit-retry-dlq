package io.github.opensanca.rabbit.config;

import java.util.List;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.opensanca.rabbit.config.properties.generic.QueueProperties;

@Configuration
public class QueueBinding {

    @Bean
    public RabbitAdmin rabbitAdmin(final RabbitTemplate rabbitTemplate, final List<QueueProperties> properties) {
        final RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        properties.forEach(p -> {
            if (p.isAutoCreate()) {
                Exchange exchange = new TopicExchange(p.getExchange(), true, false);

                if (p.getExchangeType().equals("direct")) {
                    exchange = new DirectExchange(p.getExchange(), true, false);
                } else if (p.getExchangeType().equals("fanout")) {
                    exchange = new FanoutExchange(p.getExchange(), true, false);
                }

                final Queue queue = QueueBuilder.durable(p.getQueue()).build();
                final Queue dlq = QueueBuilder.durable(p.getQueueDlq()).build();
                final Queue retry = QueueBuilder.durable(p.getQueueRetry())
                        .withArgument("x-dead-letter-exchange", p.getExchange())
                        .withArgument("x-dead-letter-routing-key", p.getQueueRoutingKey())
                        .build();

                rabbitAdmin.declareExchange(exchange);

                rabbitAdmin.declareQueue(queue);
                rabbitAdmin.declareQueue(dlq);
                rabbitAdmin.declareQueue(retry);

                rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(p.getQueueRoutingKey()).noargs());
                rabbitAdmin.declareBinding(BindingBuilder.bind(retry).to(exchange).with(p.getQueueRetry()).noargs());
                rabbitAdmin.declareBinding(BindingBuilder.bind(dlq).to(exchange).with(p.getQueueDlq()).noargs());
            }
        });

        return rabbitAdmin;
    }
}
