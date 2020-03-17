package com.suixingpay.conf.rabbitmq.destinations;

import com.suixingpay.conf.rabbitmq.type.ExchangeType;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月29日 下午10:12:10
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月29日 下午10:12:10
 */
public interface IDestination {

    /**
     * 默认名称
     */
    String DEFAULT_EXCHAGE_NAME = "";

    /**
     * 交换器名称
     * 
     * @return
     */
    AbstractExchange exchange();

    /**
     * 判断是否是默认交换器
     * 
     * @return
     */
    default boolean isDefaultExchange() {
        return DEFAULT_EXCHAGE_NAME.equals(exchange().getName());
    }

    /**
     * 交换器类型
     * 
     * @return
     */
    ExchangeType exchangeType();

    /**
     * 获取队列名称
     * 
     * @return
     */
    Queue queue();

    /**
     * @return
     */
    Binding binding();

    /**
     * 路由KEY
     * 
     * @return
     */
    String getRoutingKey();
}