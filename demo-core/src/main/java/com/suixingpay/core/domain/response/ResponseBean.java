/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 11:54 AM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.domain.response;

import com.suixingpay.core.enums.CommonExceptionEnum;
import com.suixingpay.core.exception.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 周晨<zhou_chen1@suixingpay.com>
 * @date: 2020/3/16 11:54 AM
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseBean<T> implements Serializable {

    private static final long serialVersionUID = -6027021300935759741L;
    
    /**
     * 系统响应码
     */
    private String code;
    /**
     * 系统消息
     */
    private String msg;

    /**
     * 请求时id
     */
    private String reqId;

    /**
     * 签名类型
     */
    private String signType;


    /**
     * 返回消息
     */
    private T respData;

    /**
     * 签名
     */
    private String sign;

    public ResponseBean(String code, String msg, T respData) {
        this.code = code;
        this.msg = msg;
        this.respData = respData;
    }

    public static ResponseBean ok(String message) {
        return new ResponseBean(ExceptionEnum.SUCCESS.getCode(), message, null);
    }

    public static ResponseBean ok(Object data) {
        return new ResponseBean(ExceptionEnum.SUCCESS.getCode(), null, data);
    }

    public static ResponseBean ok(String message, Object data) {
        return new ResponseBean(ExceptionEnum.SUCCESS.getCode(), message, data);
    }

    public static ResponseBean error(Exception e) {
        return new ResponseBean(ExceptionEnum.FAIL.getCode(), e.getMessage(), null);
    }

    public static ResponseBean error(String message) {
        return new ResponseBean(ExceptionEnum.FAIL.getCode(), message, null);
    }

    public static ResponseBean file(String code,String message, Object date) {
        return new ResponseBean(code, message, date);
    }

    public static <T> ResponseBean list(int total, List<T> list) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        list = CollectionUtils.isNotEmpty(list) ? list : new ArrayList<>();
        resultMap.put("list", list);
        return ok(resultMap);
    }
}
