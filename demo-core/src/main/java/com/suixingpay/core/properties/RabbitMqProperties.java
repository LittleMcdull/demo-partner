/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/10/09
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/10/09 15:36
 * @version: V1.0
 */
@Component
@Data
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitMqProperties {

    private RabbitProperties pay;

    private String host;

    private int port;

    private String username;

    private String password;

    /**
     * 消息发送到交换机确认机制,是否确认回调
     */
    private boolean publisherConfirms;

    private Integer prefetchCount;

    private Integer concurrentConsumers;

    private Integer maxConcurrentConsumers;

    private Boolean autoStartup;
}
