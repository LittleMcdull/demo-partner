/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/22
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.conf.rabbitmq.producer.SimpleMessageProducer;
import com.suixingpay.core.domain.User;
import com.suixingpay.core.enums.DestinationEnums;
import com.suixingpay.core.mapper.UserMapper;
import com.suixingpay.core.utils.UuidUtil;
import com.suixingpay.management.destination.DestinationEnum;
import com.suixingpay.management.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/22 18:32
 * @version: V1.0
 */
@Service
@Slf4j
public class IndexServiceImpl implements IndexService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SimpleMessageProducer simpleMessageProducer;

    @Override
    public boolean insertUser(User user) {

        user.setId(UuidUtil.getUuid());
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());

        log.info("|新增用户|添加数据|[{}]", JSONObject.toJSONString(user));
        if (userMapper.insert(user) > 0){
            try {
                simpleMessageProducer.sendMessage(DestinationEnum.DEMO_FANOUT_USER.getIDestination(), JSONObject.toJSONString(user));
                log.info("MQ推送消息:[{}]", JSONObject.toJSONString(user));
            }catch (Exception e){
                log.info("MQ:推送异常, 推送消息:", JSONObject.toJSONString(user));
            }
            return true;
        }
        return false;
    }

    @Override
    public User selectUser(String userName, String password) {
        log.info("|验证登录查询|用户名[{}]|密码|[{}]", userName, password);
        return userMapper.selectUser(userName, password);
    }
}
