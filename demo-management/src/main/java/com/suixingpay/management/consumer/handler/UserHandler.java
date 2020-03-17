/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/27
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.consumer.handler;

import com.suixingpay.conf.rabbitmq.consumer.MessageHandler;
import com.suixingpay.management.consumer.UserConsumerService;
import org.springframework.amqp.core.MessageProperties;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/27 18:31
 * @version: V1.0
 */
public class UserHandler extends MessageHandler<String> {

    private final UserConsumerService userConsumerService;

    public UserHandler(UserConsumerService userConsumerService) {
        this.userConsumerService = userConsumerService;
    }

    @Override
    public void onMessage(String messageId, String message, MessageProperties messageProperties) throws Exception {
        userConsumerService.receiveTopic1(message);
    }

    @Override
    public void onError(Throwable t) {
        System.err.println( "--->" + t.getMessage());
    }
}
