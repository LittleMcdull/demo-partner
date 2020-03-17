/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/12
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.consumer;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.core.domain.User;
import com.suixingpay.core.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/12/12 15:26
 * @version: V1.0
 */
@Slf4j
@Service
public class UserConsumerService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 监听消费消息
     *
     * @param message
     */
    public void receiveTopic1(String message){
        log.info("MQ接收消息： " + message);

        User user = JSONObject.parseObject(message, User.class);
        user.setUserName("MANAGE测试"+user.getUserName());

        userMapper.updateByPrimaryKeySelective(user);
    }
}




