/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/16
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.mq.consumer.service;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.core.domain.User;
import com.suixingpay.core.mapper.UserMapper;
import com.suixingpay.mq.destination.DestinationEnum;
import com.suixingpay.conf.rabbitmq.producer.SimpleMessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/16 16:03
 * @version: V1.0
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SimpleMessageProducer simpleMessageProducer;

    /**
     * 监听消费消息
     *
     * @param message
     */
    public void receiveTopic(String message) {
        log.info("MQ接收消息： " + message);
        User user = JSONObject.parseObject(message, User.class);

        user.setUserName("MQ测试"+user.getUserName());
        userMapper.updateByPrimaryKeySelective(user);
        try {
            simpleMessageProducer.sendMessage(DestinationEnum.DEMO_DIRECT_QUEUE.getIDestination(), JSONObject.toJSONString(user));
        } catch (Exception e) {
            log.info("|消息发送失败|消息内容[{}]|Exception[{}]", JSONObject.toJSONString(user), e);
        }
    }
}
