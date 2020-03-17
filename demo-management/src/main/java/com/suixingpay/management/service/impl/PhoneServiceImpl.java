/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/10/13
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.service.impl;

import com.suixingpay.core.domain.Phone;
import com.suixingpay.core.mapper.PhoneMapper;
import com.suixingpay.management.service.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/10/13 17:23
 * @version: V1.0
 */
@Service
@Slf4j
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    private PhoneMapper phoneMapper;

    @Override
    public List<Phone> selectPhones(Phone phone) {
        return phoneMapper.selectByContent(phone);
    }

    @Override
    public int selectCount(Phone phone) {
        return phoneMapper.countByContent(phone);
    }
}
