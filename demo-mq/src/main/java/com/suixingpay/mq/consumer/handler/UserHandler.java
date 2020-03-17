/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/05
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.mq.consumer.handler;

import com.suixingpay.conf.rabbitmq.consumer.MessageHandler;
import com.suixingpay.mq.consumer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/05 17:15
 * @version: V1.0
 */
@Slf4j
public class UserHandler extends MessageHandler<String> {

    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onMessage(String messageId, String message, MessageProperties messageProperties) throws Exception {

        log.info("MQ开始接收消息-->[{}]", message);
        userService.receiveTopic(message);
    }

    @Override
    public void onError(Throwable t) {
        System.err.println( "--->" + t.getMessage());
    }
}
