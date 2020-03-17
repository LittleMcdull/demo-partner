/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/10/13
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.service;

import com.suixingpay.core.domain.Phone;

import java.util.List;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/10/13 17:23
 * @version: V1.0
 */
public interface PhoneService {

    /**
     *
     * @return
     */
    List<Phone> selectPhones(Phone phone);

    /**
     *
     * @param phone
     * @return
     */
    int selectCount(Phone phone);
}
