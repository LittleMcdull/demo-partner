/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/17 11:33 AM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.advice;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/17 11:33 AM
 */
@Component
@Data
@ConfigurationProperties(prefix = "suixingpay.key")
public class KeyProperties {

   private String publicKey;

   private String privateKey;
}
