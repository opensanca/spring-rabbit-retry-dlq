package io.github.opensanca.rabbit.config.properties.generic;

import java.util.Objects;

public abstract class QueueProperties {

    public static final int THREE = 3;

    private String ttlRetryMessage = "5000";
    private Integer maxRetriesAttempts = THREE;
    private String queueRoutingKey;
    private String exchange;
    private String exchangeType;
    private String queue;
    private String queueTransport;
    private String queueRetry;
    private String queueDlq;
    private boolean defaultRetryDlq = true;
    private boolean autoCreate;

    public static int getTHREE() {
        return THREE;
    }

    public String getTtlRetryMessage() {
        return ttlRetryMessage;
    }

    public void setTtlRetryMessage(String ttlRetryMessage) {
        this.ttlRetryMessage = ttlRetryMessage;
    }

    public Integer getMaxRetriesAttempts() {
        return maxRetriesAttempts;
    }

    public void setMaxRetriesAttempts(Integer maxRetriesAttempts) {
        this.maxRetriesAttempts = maxRetriesAttempts;
    }

    public String getQueueRoutingKey() {
        return queueRoutingKey;
    }

    public void setQueueRoutingKey(String queueRoutingKey) {
        this.queueRoutingKey = queueRoutingKey;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getQueueTransport() {
        return queueTransport;
    }

    public void setQueueTransport(String queueTransport) {
        this.queueTransport = queueTransport;
    }

    public void setQueueRetry(String queueRetry) {
        this.queueRetry = queueRetry;
    }

    public void setQueueDlq(String queueDlq) {
        this.queueDlq = queueDlq;
    }

    public boolean isDefaultRetryDlq() {
        return defaultRetryDlq;
    }

    public void setDefaultRetryDlq(boolean defaultRetryDlq) {
        this.defaultRetryDlq = defaultRetryDlq;
    }

    public boolean isAutoCreate() {
        return autoCreate;
    }

    public void setAutoCreate(boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    public String getQueueRetry() {
        return defaultRetryDlq || Objects.isNull(queueRetry) ? this.queue + "_retry" : queueRetry;
    }

    public String getQueueDlq() {
        return defaultRetryDlq || Objects.isNull(queueDlq) ? this.queue + "_dlq" : queueDlq;
    }
}
