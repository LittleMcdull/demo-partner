/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 6:30 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.advice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.suixingpay.core.annotations.ApiSecurity;
import com.suixingpay.core.bean.Content;
import com.suixingpay.core.bean.response.ResponseBean;
import com.suixingpay.core.enums.ApiSecurityException;
import com.suixingpay.core.enums.CommonExceptionEnum;
import com.suixingpay.core.utils.sign.RSASignature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 6:30 PM
 */
@Slf4j
@ControllerAdvice(basePackages = "com.suixingpay.management.api")
public class RequestAdvice implements RequestBodyAdvice {

    @Autowired
    private KeyProperties keyProperties;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        try {
            boolean encode = false;
            if (parameter.getMethod().isAnnotationPresent(ApiSecurity.class)) {
                //获取注解配置的包含和去除字段
                ApiSecurity cloudPayAnnotation = parameter.getMethodAnnotation(ApiSecurity.class);
                //是否解密
                encode = cloudPayAnnotation.encode();
            }
            if (encode) {
                return new MyHttpInputMessage(inputMessage, parameter);
            } else {
                return inputMessage;
            }
        } catch (ApiSecurityException e) {
            log.warn("请求异常：", e);
            throw e;
        } catch (Exception e) {
            log.warn("请求异常：", e);
            throw new ApiSecurityException(CommonExceptionEnum.SYS_EXCEPTION, null);
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage, MethodParameter parameter) throws Exception {

            inputMessage.getHeaders().setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
            this.headers = inputMessage.getHeaders();
            //1、验签
            String data = IOUtils.toString(inputMessage.getBody(), Content.ENCODING_UTF_8);
            log.info("验签前参数：{}",data);
            //TODO 接口权限拦截
            HashMap<String, Object> objMap=  JSONObject.parseObject(data,LinkedHashMap.class, Feature.OrderedField);
            Object reqIdObj = objMap.get("reqId");
            if(StringUtils.isEmpty(reqIdObj)){
                throw new ApiSecurityException(CommonExceptionEnum.VALIDATE_EXCEPTION,null);
            }
            String reqId = reqIdObj.toString();
            MDC.put(Content.PARAM, reqId);

            Object reqSign = objMap.get("sign");
            if(StringUtils.isEmpty(reqSign)){
                throw new ApiSecurityException(CommonExceptionEnum.VALIDATE_EXCEPTION,reqId);
            }
            //TODO 后续签名方式修改
            Object signType = objMap.get("signType");

            if(StringUtils.isEmpty(signType)){
                throw new ApiSecurityException(CommonExceptionEnum.VALIDATE_EXCEPTION,reqId);
            }

            //后续版本控制
            Object version = objMap.get("version");
            if(StringUtils.isEmpty(version)){
                throw new ApiSecurityException(CommonExceptionEnum.VALIDATE_EXCEPTION,reqId);
            }

            if (!org.apache.commons.lang3.StringUtils.equalsAny(version.toString(), Content.VERSION)) {
                throw new ApiSecurityException(CommonExceptionEnum.VALIDATE_EXCEPTION,reqId);
            }


            Object timestamp = objMap.get("timestamp");
            if(StringUtils.isEmpty(timestamp)){
                throw new ApiSecurityException(CommonExceptionEnum.VALIDATE_EXCEPTION,reqId);
            }

            Object reqData =objMap.get("reqData");
            if(StringUtils.isEmpty(reqData)){
                throw new ApiSecurityException(CommonExceptionEnum.VALIDATE_EXCEPTION,reqId);
            }

            //TODO 从机构信息获取
            objMap.remove("sign");
            String content = RSASignature.getOrderContent(objMap);
            log.info("验签字符串[{}]", content);
            //TODO 解签( rsa签名，并与上送的 sign 比对)
            boolean verify = RSASignature.doCheck(content, reqSign.toString(), keyProperties.getPublicKey());
            log.debug("验签结果：{}",verify);
            if (!verify) {
                //TODO 如果失败从库里读取防止没有刷新缓存
                throw new ApiSecurityException(CommonExceptionEnum.SXF0005,reqId);
            }

            JSONObject reqDataJson = JSONObject.parseObject(reqData.toString());
            objMap.put("reqData",reqDataJson);
            //TODO  公共方法封装给接口用
            String jsonObj = JSON.toJSONString(objMap);
            this.body = IOUtils.toInputStream(jsonObj, Content.ENCODING_UTF_8);
            log.info("验签封装后参数：{}",jsonObj);
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }

    @ResponseBody
    @ApiSecurity
    @ExceptionHandler(ApiSecurityException.class)
    public ResponseBean<Object> handleException(ApiSecurityException e) {
        // 记录错误信息
        log.error(e.getMessage());
        return new ResponseBean<>(e.getCode(),e.getMessage(),e.getReqId());
    }
}
