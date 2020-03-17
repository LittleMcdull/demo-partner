/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/17 4:14 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suixingpay.core.annotations.ApiSecurity;
import com.suixingpay.core.bean.response.ResponseBean;
import com.suixingpay.core.enums.CommonExceptionEnum;
import com.suixingpay.core.utils.sign.RSASignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

/**
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/17 4:14 PM
 */
@Slf4j
@ControllerAdvice(basePackages = "com.suixingpay.management.api")
public class ResponseAdvice implements ResponseBodyAdvice {

    @Value("${suixingpay.key.privateKey}")
    private String privateKey;


    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        boolean encode = false;
        if (returnType.getMethod().isAnnotationPresent(ApiSecurity.class)) {
            //获取注解配置的包含和去除字段
            ApiSecurity apiSecurity = returnType.getMethodAnnotation(ApiSecurity.class);
            //是否加密
            encode = apiSecurity.encode();
        }
        //返回数据
        String bodyStr = JSON.toJSONString(body);
        log.info("返回参数：{}",bodyStr);
        if (encode) {
            try {
                JSONObject obj = JSONObject.parseObject(bodyStr);
                log.info("对方法method :" + returnType.getMethod().getName() + "返回数据进行加密");
                //签名
                Map<String,Object> paramMap = obj;
                String content = RSASignature.getOrderContent(paramMap);
                log.info("resultcontentSign:"+content);
                String retSign = RSASignature.encryptBASE64(RSASignature.sign(content,privateKey));
                obj.put("sign", retSign);
                log.info("返回加密参数：{}",obj);
                return obj;
            } catch (Exception e) {
                log.error("返回数据解析异常:{}", e.getMessage());
                return new ResponseBean<>(CommonExceptionEnum.SYS_EXCEPTION.getCode(), CommonExceptionEnum.SYS_EXCEPTION.getMessage(),null);
            }
        }
        //返回数据
        log.debug("debug返回数据："+body);
        return body;
    }
}
