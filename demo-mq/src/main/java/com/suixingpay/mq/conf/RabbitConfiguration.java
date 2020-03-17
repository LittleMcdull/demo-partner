/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/05
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.mq.conf;

import com.suixingpay.conf.rabbitmq.consumer.MessageHandler;
import com.suixingpay.conf.rabbitmq.consumer.SimpleMessageConsumer;
import com.suixingpay.mq.consumer.handler.UserHandler;
import com.suixingpay.mq.consumer.service.UserService;
import com.suixingpay.mq.destination.DestinationEnum;
import com.suixingpay.mq.properties.RabbitMqProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/05 15:45
 * @version: V1.0
 */
@Slf4j
@Configuration
public class RabbitConfiguration {

    @Autowired
    private RabbitMqProperties props;

    private static final String payConnectionFactoryName = "payConnectionFactory";

    private static final String payAmqpAdmin = "payAmqpAdmin";

    private CachingConnectionFactory rabbitConnectionFactory(RabbitProperties config)
            throws Exception {
        RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
        if (config.determineHost() != null) {
            factory.setHost(config.determineHost());
        }
        factory.setPort(config.determinePort());
        if (config.determineUsername() != null) {
            factory.setUsername(config.determineUsername());
        }
        if (config.determinePassword() != null) {
            factory.setPassword(config.determinePassword());
        }
        if (config.determineVirtualHost() != null) {
            factory.setVirtualHost(config.determineVirtualHost());
        }
        if (config.getRequestedHeartbeat() != null) {
            factory.setRequestedHeartbeat(config.getRequestedHeartbeat());
        }
        RabbitProperties.Ssl ssl = config.getSsl();
        if (ssl.isEnabled()) {
            factory.setUseSSL(true);
            if (ssl.getAlgorithm() != null) {
                factory.setSslAlgorithm(ssl.getAlgorithm());
            }
            factory.setKeyStore(ssl.getKeyStore());
            factory.setKeyStorePassphrase(ssl.getKeyStorePassword());
            factory.setTrustStore(ssl.getTrustStore());
            factory.setTrustStorePassphrase(ssl.getTrustStorePassword());
        }
        if (config.getConnectionTimeout() != null) {
            factory.setConnectionTimeout(config.getConnectionTimeout());
        }
        factory.afterPropertiesSet();
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(
                factory.getObject());
        connectionFactory.setAddresses(config.determineAddresses());
        connectionFactory.setPublisherConfirms(config.isPublisherConfirms());
        connectionFactory.setPublisherReturns(config.isPublisherReturns());
        if (config.getCache().getChannel().getSize() != null) {
            connectionFactory
                    .setChannelCacheSize(config.getCache().getChannel().getSize());
        }
        if (config.getCache().getConnection().getMode() != null) {
            connectionFactory
                    .setCacheMode(config.getCache().getConnection().getMode());
        }
        if (config.getCache().getConnection().getSize() != null) {
            connectionFactory.setConnectionCacheSize(
                    config.getCache().getConnection().getSize());
        }
        if (config.getCache().getChannel().getCheckoutTimeout() != null) {
            connectionFactory.setChannelCheckoutTimeout(
                    config.getCache().getChannel().getCheckoutTimeout());
        }
        return connectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter());
    }

    @Bean(name=payConnectionFactoryName)
    @Primary
    public ConnectionFactory payConnectionFactory() throws Exception{
        CachingConnectionFactory connectionFactory = rabbitConnectionFactory(props.getPay());
        log.info("==========payHost:{}",connectionFactory.getHost());
        return connectionFactory;
    }


    @Bean(name=payAmqpAdmin)
    public RabbitAdmin payAmqpAdmin(@Qualifier(payConnectionFactoryName) ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    private SimpleMessageConsumer<String> config(DestinationEnum destination, MessageHandler<String> handle, ConnectionFactory connectionFactory
            , RabbitAdmin rabbitAdmin) {
        SimpleMessageConsumer<String> consumer = new SimpleMessageConsumer<String>(connectionFactory, destination.getIDestination(), handle,true);
        consumer.setPrefetchCount(props.getPrefetchCount());
        consumer.setConcurrentConsumers(props.getConcurrentConsumers());
        consumer.setMaxConcurrentConsumers(props.getMaxConcurrentConsumers());
        consumer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        consumer.setRabbitAdmin(rabbitAdmin);
        return consumer;
    }

    @Bean
    public SimpleMessageConsumer<String> fanoutUserConsumer(@Qualifier("userService") UserService userService,
                                                                   @Qualifier(payConnectionFactoryName)ConnectionFactory connectionFactory, @Qualifier(payAmqpAdmin) RabbitAdmin rabbitAdmin  ) {
        return config(DestinationEnum.DEMO_FANOUT_USER,new UserHandler(userService),connectionFactory,rabbitAdmin);
    }
}

