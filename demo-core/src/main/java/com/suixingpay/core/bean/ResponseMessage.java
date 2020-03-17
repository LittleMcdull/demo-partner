/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/17 12:04 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.bean;

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
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/17 12:04 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = -6970675031171558256L;

    private static final int CODE_SUCCESS = 200;

    private static final int CODE_ERROR = 500;

    private int code;

    private String message;

    private Object data;

    public static ResponseMessage ok(String message) {
        return new ResponseMessage(CODE_SUCCESS, message, null);
    }

    public static ResponseMessage ok(Object data) {
        return new ResponseMessage(CODE_SUCCESS, null, data);
    }

    public static ResponseMessage ok(String message, Object data) {
        return new ResponseMessage(CODE_SUCCESS, message, data);
    }

    public static ResponseMessage error(Exception e) {
        return new ResponseMessage(CODE_ERROR, e.getMessage(), null);
    }

    public static ResponseMessage error(String message) {
        return new ResponseMessage(CODE_ERROR, message, null);
    }

    public static ResponseMessage file(int code,String message,Object date) {
        return new ResponseMessage(code, message, date);
    }

    public static <T> ResponseMessage list(int total, List<T> list) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        list = CollectionUtils.isNotEmpty(list) ? list : new ArrayList<>();
        resultMap.put("list", list);
        return ok(resultMap);
    }
}
