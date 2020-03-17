/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/17 11:21 AM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.constants;

import com.suixingpay.api.domain.request.RequestRpc;
import com.suixingpay.core.bean.Content;
import com.suixingpay.core.utils.DateUtils;
import com.suixingpay.core.utils.UuidUtil;

/**
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/17 11:21 AM
 */
public class BeanConvert {

    /**
     * 绑定公共请求体
     *
     * @param <T>
     * @return
     */
    public static <T> RequestRpc<T>  buildRequest(){
        RequestRpc<T> requestRpc = new RequestRpc<>();
        requestRpc.setReqId(UuidUtil.getUuid());
        requestRpc.setVersion(Content.VERSION);
        requestRpc.setSignType(Content.RSA);
        requestRpc.setTimestamp(DateUtils.C_DATE_TIME_FORMAT());
        return requestRpc;
    }
}
