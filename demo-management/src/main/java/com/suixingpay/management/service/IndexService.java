/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/22
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.service;

import com.suixingpay.core.domain.User;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/22 18:31
 * @version: V1.0
 */
public interface IndexService {

    /**
     * 添加注册
     * @param user
     * @return
     */
    boolean insertUser(User user);

    /**
     * 验证登陆
     * @param userName
     * @param password
     * @return
     */
    User selectUser(String userName, String password);
}
