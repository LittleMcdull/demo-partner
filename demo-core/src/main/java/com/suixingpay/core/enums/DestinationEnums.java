/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/16
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/16 14:56
 * @version: V1.0
 */
@Getter
@AllArgsConstructor
public enum DestinationEnums {
    DEMO_USER_DIRECT("demo.user.exchange", "demo.user.queue", "demo.user.exchange.key"),
    DEMO_USER_FANOUT("demo.user.fanout", "demo.user.queue", ""),
    DEMO_TEST_FANOUT("demo.user.fanout", "demo.test.fanout", "");

    /**
     * 交换机名称
     */
    private String exchangeName;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 路由routingKey
     */
    private String routingKey;
}
