/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 5:53 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.annotations;


import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 5:53 PM
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiSecurity {

    /**
     * 解析处理
     *
     * @return
     */
    boolean encode() default true;

    /**
     * 是否加签验签
     *
     * @return
     */
    boolean sign() default true;
}
