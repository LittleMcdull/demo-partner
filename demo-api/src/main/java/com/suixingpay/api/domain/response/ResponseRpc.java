/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 11:36 AM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.api.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 周晨<zhou_chen1@suixingpay.com>
 * @date: 2020/3/16 11:36 AM
 */
@Data
@AllArgsConstructor
public class ResponseRpc<T> {

    private static final long serialVersionUID = -6970675031171558256L;

    private static final String CODE_SUCCESS = "200";

    private static final String CODE_ERROR = "500";

    private String code;

    private String message;

    private T data;

    public static ResponseRpc ok(String message) {
        return new ResponseRpc(CODE_SUCCESS, message, null);
    }

    public static ResponseRpc ok(Object data) {
        return new ResponseRpc(CODE_SUCCESS, null, data);
    }

    public static ResponseRpc ok(String message, Object data) {
        return new ResponseRpc(CODE_SUCCESS, message, data);
    }

    public static ResponseRpc error(Exception e) {
        return new ResponseRpc(CODE_ERROR, e.getMessage(), null);
    }

    public static ResponseRpc error(String message) {
        return new ResponseRpc(CODE_ERROR, message, null);
    }

    public static ResponseRpc error(String code,String message,Object date) {
        return new ResponseRpc(code, message, date);
    }

    public static <T> ResponseRpc list(int total, List<T> list) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("total", total);
        resultMap.put("list", list);
        return ok(resultMap);
    }
}
