/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/25
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.exception.enums;

import lombok.Getter;

/**
 * @Description: 异常状态码
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/25 18:48
 * @version: V1.0
 */
@Getter
public enum  ExceptionEnum {

    SUCCESS("0000", "SUCCESS"),
    FAIL("0001", "FAIL");

    private String code;

    private String desc;

    ExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
