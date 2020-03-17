/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/23
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.mq.destination;

import com.suixingpay.conf.rabbitmq.destinations.DirectDestination;
import com.suixingpay.conf.rabbitmq.destinations.FanoutDestination;
import com.suixingpay.conf.rabbitmq.destinations.IDestination;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/23 14:06
 * @version: V1.0
 */
public enum DestinationEnum {
    DEMO_FANOUT_USER(new FanoutDestination("demo.user.fanout","demo.user.queue")),
    DEMO_FANOUT_TEST(new FanoutDestination("demo.user.fanout","demo.test.fanout")),
    DEMO_DIRECT_QUEUE(new DirectDestination("demo.user.exchange","demo.direct.queue")),
    ;
    private final IDestination iDestination;

    private final String exchange;

    DestinationEnum(IDestination iDestination){
        this.iDestination = iDestination;
        this.exchange = name();
    }

    DestinationEnum(IDestination iDestination, String exchange){
        this.iDestination = iDestination;
        this.exchange = exchange;
    }

    public String getName() {
        return name();
    }

    public String getExchange() {
        return this.exchange;
    }

    public IDestination getIDestination() {
        return this.iDestination;
    }
}
