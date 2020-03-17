package com.suixingpay.conf.rabbitmq.retry;

import java.io.Serializable;

import org.springframework.amqp.core.Message;

import lombok.Data;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年04月10日 17时35分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年04月10日 17时35分
 */
@Data
public class MessageCache implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String exchangeName;

    private String routingKey;

    private Message message;

    private long firstSendTime;

    private long lastSendTime;

    /**
     * 消息是否已发送
     */
    private volatile boolean sended = false;
}