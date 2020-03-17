package com.suixingpay.conf.rabbitmq.retry;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.suixingpay.conf.rabbitmq.converter.JsonMessageConverter;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年07月11日 10时17分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年07月11日 10时17分
 */
@Setter
@Getter
public class MessageContent implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String exchangeName;

    private String routingKey;

    private String message;

    private String firstSendTime;

    private String lastSendTime;

    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    public MessageContent(MessageCache messageCache) {
        if (null == messageCache) {
            throw new RuntimeException("messageCache is null");
        }
        this.id = messageCache.getId();
        this.exchangeName = messageCache.getExchangeName();
        this.routingKey = messageCache.getRoutingKey();
        this.message = JsonMessageConverter.getMessageContent(messageCache.getMessage());
        DateFormat dateFormat = new SimpleDateFormat(FORMAT);
        this.firstSendTime = dateFormat.format(new Date(messageCache.getFirstSendTime()));
        this.lastSendTime = dateFormat.format(new Date(messageCache.getLastSendTime()));
    }
}