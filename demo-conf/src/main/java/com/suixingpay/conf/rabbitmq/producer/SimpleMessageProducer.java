package com.suixingpay.conf.rabbitmq.producer;

import com.suixingpay.conf.rabbitmq.converter.JsonMessageConverter;
import com.suixingpay.conf.rabbitmq.destinations.IDestination;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月30日 下午2:41:48
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月30日 下午2:41:48
 */
@Slf4j
@Setter
@Getter
@Component
public final class SimpleMessageProducer implements RabbitTemplate.ConfirmCallback, ReturnCallback {
    private static final JsonMessageConverter messageConverter = new JsonMessageConverter();
    /**
     * 用于发送消息
     */
    private final RabbitTemplate rabbitTemplate;

    /**
     * 消息回调
     */
    private MessageProducerCallback callback;

    /**
     * 生产者名称，用于告诉消费者消息是由哪个应用产生的， 默认是spring.application.name
     */
    private String appName;

    /**
     * 发送消息超时时间，如果超过此值，则打印错误日志
     */
    private int sendTimeout = 1500;

    /**
     * @param rabbitTemplate
     */
    public SimpleMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(messageConverter);
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 成功发送到RabbitMQ 交换器中，注意，此时还没有发送到消息者中
        if (ack) {
            if (log.isDebugEnabled()) {
                log.debug("消息 {} 已经成功发送到交换器中", correlationData.getId());
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("消息 {} 因{} 未能发送到交换器中", correlationData.getId(), cause);
            }
        }
        if (null != callback) {
            callback.onConfirm(correlationData.getId(), ack, cause);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        Object messageData = this.rabbitTemplate.getMessageConverter().fromMessage(message);
        MessageProperties messageProperties = message.getMessageProperties();
        if (log.isWarnEnabled()) {
            log.warn("消息:{} 发送到交换器:{}后，无法根据自身的类型和路由键:{}找到一个符合条件的队列，replyCode：{}, replyText {}.，详细信息：{}", messageData,
                    exchange, routingKey, replyCode, replyText, messageProperties);
        }
        String messageId = null;
        if (null != messageProperties) {
            messageId = messageProperties.getMessageId();
        }
        if (null != callback) {
            callback.onReturned(messageId, messageData, replyCode, replyText, exchange, routingKey);
        }
    }

    /**
     * @param object
     * @param messageProperties
     * @return
     */
    private Message convertMessageIfNecessary(final Object object, MessageProperties messageProperties) {
        if (object instanceof Message) {
            return (Message) object;
        }
        if (null == messageProperties) {
            messageProperties = new MessageProperties();
        }
        return getRequiredMessageConverter().toMessage(object, messageProperties);
    }

    /**
     * @return
     * @throws IllegalStateException
     */
    private MessageConverter getRequiredMessageConverter() throws IllegalStateException {
        MessageConverter converter = this.rabbitTemplate.getMessageConverter();
        if (converter == null) {
            throw new AmqpIllegalStateException(
                    "No 'messageConverter' specified. Check configuration of RabbitTemplate.");
        }
        return converter;
    }

    /**
     * @param destination       IDestination实例
     * @param message           消息内容
     * @param messageProperties MessageProperties，用于设置消息属性
     * @throws Exception
     */
    public void sendMessage(IDestination destination, Object message, MessageProperties messageProperties)
            throws Exception {
        String messageId = null;
        if (null != messageProperties) {
            messageId = messageProperties.getMessageId();
        }
        if (null == messageId || messageId.isEmpty()) {
            messageId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        String routingKey = destination.getRoutingKey();
        if (null != routingKey && routingKey.indexOf('*') != -1 && routingKey.indexOf('#') != -1) {
            throw new Exception("路由键\"" + routingKey + "\"中包含有*或#字符!");
        }

        try {
            Message sendMessage = convertMessageIfNecessary(message, messageProperties);
            messageProperties = sendMessage.getMessageProperties();
            if (null != messageProperties) {
                if (null == messageProperties.getMessageId()) {
                    messageProperties.setMessageId(messageId);
                }
                if (null == messageProperties.getCorrelationIdString()) {
                    messageProperties.setCorrelationIdString(messageId);
                }
                messageProperties.setAppId(appName);
                messageProperties.setTimestamp(new Date());
            }
            String exchangeName = destination.exchange().getName();
            doSendMessage(exchangeName, destination.getRoutingKey(), sendMessage, messageId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw e;
        }
    }

    /**
     * @param exchangeName
     * @param routingKey
     * @param message
     * @param messageId
     */
    public void doSendMessage(String exchangeName, String routingKey, Message message, String messageId) {
        long start = System.currentTimeMillis();
        rabbitTemplate.send(exchangeName, routingKey, message, new CorrelationData(messageId));
        long useTime = System.currentTimeMillis() - start;
        if (sendTimeout > 0 && useTime > sendTimeout) {
            log.error("send message to exchangeName:{}, routingKey:{}, use time:{}", exchangeName, routingKey, useTime);
        }
    }

    /**
     * @param destination
     * @param message
     * @throws Exception
     */
    public void sendMessage(IDestination destination, Object message) throws Exception {
        sendMessage(destination, message, null);
    }

}