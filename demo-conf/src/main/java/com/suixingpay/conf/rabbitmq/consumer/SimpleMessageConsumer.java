package com.suixingpay.conf.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.suixingpay.conf.rabbitmq.destinations.IDestination;
import com.suixingpay.conf.rabbitmq.destinations.ITraceNumManager;
import com.suixingpay.conf.rabbitmq.converter.JsonMessageConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ErrorHandler;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消费者
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月30日 下午3:27:45
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月30日 下午3:27:45
 */
@Slf4j
@Getter
public final class SimpleMessageConsumer<T> extends SimpleMessageListenerContainer {

    private final IDestination destination;

    @Autowired(required = false)
    private ITraceNumManager traceNumManager;

    /**
     * 收到消息后立即关闭
     */
    private volatile boolean autoStop = false;

    private volatile int autoStopLimit = 1;

    private AtomicInteger counter = new AtomicInteger();

    /**
     * 消息消费耗时
     */
    private int timeout = 1000;

    /**
     * 生产方到消费投递耗时
     */
    private int postTimeout = 2000;

    private MessageConversionExceptionHandleType messageConversionExceptionHandleType = MessageConversionExceptionHandleType.ACK;

    /**
     * @param connectionFactory
     * @param destination
     * @param messageHandler
     * @param autoGetAmqpMessage
     */
    public SimpleMessageConsumer(ConnectionFactory connectionFactory, IDestination destination,
                                 MessageHandler<T> messageHandler, boolean autoGetAmqpMessage) {
        if (null == destination) {
            throw new IllegalArgumentException("destination 参数不能为null");
        }
        if (null == messageHandler) {
            throw new IllegalArgumentException("messageHandler 参数不能为null");
        }
        this.destination = destination;
        this.setConnectionFactory(connectionFactory);

        MessageHandlerAdapter<T> handlerAdapter = new MessageHandlerAdapter<>(this, messageHandler, traceNumManager, destination);
        this.setErrorHandler(handlerAdapter);
        this.setMessageListener(handlerAdapter);
        JsonMessageConverter converter = new JsonMessageConverter(messageHandler.getType());
        converter.setAutoGetAmqpMessage(autoGetAmqpMessage);
        this.setMessageConverter(converter);
    }

    /**
     * @param connectionFactory
     * @param destination
     * @param messageHandler
     */
    public SimpleMessageConsumer(ConnectionFactory connectionFactory, IDestination destination,
                                 MessageHandler<T> messageHandler) {
        this(connectionFactory, destination, messageHandler, true);
    }

    public boolean isAutoStop() {
        return autoStop;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setPostTimeout(int postTimeout) {
        this.postTimeout = postTimeout;
    }

    public int getAutoStopLimit() {
        return autoStopLimit;
    }

    public void setAutoStopLimit(Integer autoStopLimit) {
        if (null == autoStopLimit || autoStopLimit.intValue() <= 0) {
            this.autoStopLimit = 1;
        } else {
            this.autoStopLimit = autoStopLimit.intValue();
        }
        counter.set(0);
    }

    public int getAutoStopCounter() {
        return this.counter.get();
    }

    public MessageConversionExceptionHandleType getMessageConversionExceptionHandleType() {
        return messageConversionExceptionHandleType;
    }

    public void setMessageConversionExceptionHandleType(MessageConversionExceptionHandleType messageConversionExceptionHandleType) {
        this.messageConversionExceptionHandleType = messageConversionExceptionHandleType;
    }

    public void resetCounter() {
        counter.set(0);
    }

    public void setAutoStop(boolean autoStop) {
        if (this.getAcknowledgeMode().isManual()) {
            counter.set(0);
        }
        this.autoStop = autoStop;
    }

    /**
     *
     */
    @PostConstruct
    public void declareAll() {
        try {
            RabbitAdmin rabbitAdmin = this.getRabbitAdmin();
            if (null == rabbitAdmin) {
                rabbitAdmin = (RabbitAdmin) this.getApplicationContext().getBean(AmqpAdmin.class);
                // 支持多个RabbitMQ
                if (rabbitAdmin.getRabbitTemplate().getConnectionFactory() != this.getConnectionFactory()) {
                    rabbitAdmin = new RabbitAdmin(this.getConnectionFactory());
                }
                this.setRabbitAdmin(rabbitAdmin);
            }
            if (!destination.isDefaultExchange()) {
                rabbitAdmin.declareExchange(destination.exchange());
            }
            rabbitAdmin.declareQueue(destination.queue());
            rabbitAdmin.declareBinding(destination.binding());
            this.setQueues(destination.queue());
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param <T>
     */
    @Slf4j
    private static class MessageHandlerAdapter<T> implements ChannelAwareMessageListener, ErrorHandler {

        private final SimpleMessageConsumer<T> consumer;

        private final MessageHandler<T> handler;

        private final ITraceNumManager traceNumManager;

        private final String traceNumPrefix;

        protected MessageHandlerAdapter(SimpleMessageConsumer<T> consumer, MessageHandler<T> handler, ITraceNumManager traceNumManager, IDestination destination) {
            this.consumer = consumer;
            this.handler = handler;
            this.traceNumManager = traceNumManager;
            this.traceNumPrefix = destination.exchange().getName() + "-" + destination.queue().getName();
        }

        @Override
        public void onMessage(org.springframework.amqp.core.Message message, Channel channel) throws Exception {
            MessageProperties messageProperties = message.getMessageProperties();
            String messageId = null;
            String appId = null;
            Date timestamp = null;
            if (null != messageProperties) {
                messageId = messageProperties.getMessageId();
                appId = messageProperties.getAppId();
                timestamp = messageProperties.getTimestamp();
            }
            long start = System.currentTimeMillis();
            boolean canStop = false;
            if (null != traceNumManager) {
                String traceNum = traceNumManager.gen(traceNumPrefix);
                traceNumManager.beginTrace(traceNum);
            }
            if (null != timestamp) {
                // 消息年龄
                long messageAge = System.currentTimeMillis() - timestamp.getTime();
                // 如果消息年龄过大
                if (messageAge > consumer.postTimeout) {
                    log.error("queue:{}, exchange:{}, messageId:{}, from app:{}, message age:{}ms",
                            consumer.destination.queue().getName(), consumer.destination.exchange().getName(),
                            messageId, appId, messageAge);
                } else if (log.isDebugEnabled()) {
                    log.debug("queue:{}, exchange:{}, messageId:{}, from app:{}, message age:{}ms",
                            consumer.destination.queue().getName(), consumer.destination.exchange().getName(),
                            messageId, appId, messageAge);
                }
            }
            switch (consumer.getAcknowledgeMode()) {
                case MANUAL:
                    boolean printErrorLog = true;
                    try {
                        if (consumer.autoStop && consumer.autoStopLimit > 0) {
                            int currentCount = consumer.counter.incrementAndGet();
                            if (currentCount > consumer.autoStopLimit) {
                                String errorMsg = "手动ACK模式下，开启自动停止消费时，已消费" + consumer.autoStopLimit + "条消息，多余消息不进行消费。";
                                log.warn(errorMsg);
                                printErrorLog = false;
                                canStop = true;
                                throw new Exception(errorMsg);
                            }
                        }
                        @SuppressWarnings("unchecked")
                        T object = (T) consumer.getMessageConverter().fromMessage(message);
                        handler.onMessage(messageId, object, messageProperties);
                        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); // 确认消息成功消费
                    } catch (MessageConversionException ex) {
                        switch (consumer.messageConversionExceptionHandleType) {
                            case NOT_ACK:
                                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);// ack返回false，并重新回到队列
                                break;
                            case ACK:
                                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); // 确认消息成功消费
                                break;
                        }
                        log.error(messageId + ":" + ex.getMessage(), ex);
                    } catch (Throwable e) {
                        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);// ack返回false，并重新回到队列
                        if (printErrorLog) {
                            log.error(messageId + ":" + e.getMessage(), e);
                        }
                        // handler.onError(e);
                    }
                    break;
                default:
                    canStop = consumer.autoStop;
                    try {
                        @SuppressWarnings("unchecked")
                        T object = (T) consumer.getMessageConverter().fromMessage(message);
                        handler.onMessage(messageId, object, messageProperties);
                    } catch (Throwable e) {
                        log.error(messageId + ":" + e.getMessage(), e);
                        // handler.onError(e);
                    }
                    break;
            }

            long useTime = System.currentTimeMillis() - start;
            if (useTime > consumer.timeout) {
                log.error("MessageConsumer use time: " + useTime);
            }
            if (canStop) {
                consumer.stop();
            }
            if (null != traceNumManager) {
                traceNumManager.endTrace();
            }

        }

        @Override
        public void handleError(Throwable t) {
            log.error(t.getMessage(), t);
            handler.onError(t);
        }
    }
}