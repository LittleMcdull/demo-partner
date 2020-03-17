/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 11:35 AM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.api.domain.request;

import lombok.Data;

/**
 * @author: 周晨<zhou_chen1@suixingpay.com>
 * @date: 2020/3/16 11:35 AM
 */
@Data
public class RequestRpc<T> {

    /**
     * 请求id
     */
    private String reqId;

    /**
     * 请求接口版本1.0
     */
    private String version;

    /**
     * 业务数据
     */
    private T reqData;

    /**
     * 签名
     */
    private String sign;

    /**
     * 签名类型
     */
    private String signType;

    /**
     * 请求时间
     */
    private String timestamp;
}
