/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/10/13
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.controller;

import com.suixingpay.core.bean.ResponseMessage;
import com.suixingpay.core.domain.Phone;
import com.suixingpay.management.service.PhoneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/10/13 17:30
 * @version: V1.0
 */
@RestController
@RequestMapping("/phone")
@CrossOrigin
@Slf4j
@Api(description = "手机管理界面")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @GetMapping("/list")
    @ApiOperation(value = "手机管理界面列表", notes = "手机管理界面列表")
    public ResponseMessage list(Phone phone){

        return ResponseMessage.list(phoneService.selectCount(phone),
                phoneService.selectPhones(phone));
    }
}
