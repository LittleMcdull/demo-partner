/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 4:51 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedHashMap;
import java.util.Map;

/** 加签验签工具类
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 4:51 PM
 */
@Slf4j
public class SignUtils {
    /**
     * 自己私钥
     */
    private static String privateKey;

    /**
     * 对方公钥
     */
    private static String publicKey;

    public static JSONObject post(Object data, String url) throws Exception {

        // 加签
        JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(data));

        String signStr = RSASignature.sign(parseObject.toJSONString(), getPrivateKey(), "UTF-8");
        parseObject.put("sign", signStr);

        JSONObject result = HttpUtils.connectPostUrl(url, parseObject.toJSONString());

        if ("0".equals(result.getString("rpCd"))) {
            // 验签
            if (!checkSign(result)) {
                log.info("验签失败");
                throw new RuntimeException("验签失败");
            }

            result.remove("sign");
        }
        return result;
    }

    /**
     * 带header的请求
     *
     * @param url
     * @param data
     * @param map
     * @return
     * @throws Exception
     */
    public static JSONObject connectPostUrl(String url, Object data, Map<String, String> map) {

        // 加签
        JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(data));

        String signStr = RSASignature.sign(parseObject.toJSONString(), getPrivateKey(), "UTF-8");
        parseObject.put("sign", signStr);

        JSONObject result = new JSONObject();
        try {
            result = HttpUtils.connectPostUrl(url, parseObject.toJSONString(), map);
        } catch (Exception e) {
            log.info("调用HttpUtils.connectPostUrl异常，{}", e);
        }

        if ("0".equals(result.getString("rpCd"))) {
            // 验签

            if (!checkSign(result)) {
                log.info("验签失败");
                throw new RuntimeException("验签失败");
            }

            result.remove("sign");
        }
        return result;
    }

    /**
     * 带header的请求
     *
     * @param url
     * @param data
     * @param map
     * @return
     * @throws Exception
     */
    public static JSONObject connectPostUrl(String url, Object data, Map<String, String> map, int readTime) throws Exception {

        // 加签
        JSONObject parseObject = JSONObject.parseObject(JSONObject.toJSONString(data));

        String signStr = RSASignature.sign(parseObject.toJSONString(), getPrivateKey(), "UTF-8");
        parseObject.put("sign", signStr);

        JSONObject result = new JSONObject();
        try {
            result = HttpUtils.connectPostUrl(url, parseObject.toJSONString(), map, readTime);
        } catch (Exception e) {
            log.info("调用HttpUtils.connectPostUrl异常，{}", e);
            throw e;
        }

        if ("0".equals(result.getString("rpCd"))) {
            // 验签

            if (!checkSign(result)) {
                log.info("验签失败");
                throw new RuntimeException("验签失败");
            }

            result.remove("sign");
        }
        return result;
    }


    public static boolean checkSign(JSONObject obj) {
        Map<String, Object> paramMap = obj;
        if (null != paramMap.get("sign")) {
            String sign = paramMap.get("sign").toString();
            paramMap.remove("sign");
            String content = RSASignature.getOrderContent(paramMap);
            return RSASignature.doCheck(content, sign, getPublicKey());
        } else {
            return false;
        }
    }

    /**
     * 验证签名正确性
     *
     * @param
     */
    public static boolean checkSign(String orginalData) {
        log.debug("请求数据:[{}]", orginalData);

        Map<String, Object> paramsMap = JSONObject.parseObject(orginalData, LinkedHashMap.class, Feature.OrderedField);

        String sign = paramsMap.get("sign").toString();
        paramsMap.remove("sign");
        String content = RSASignature.getOrderContent(paramsMap);
        log.info("验签数据:[{}]", content);

        return RSASignature.doCheck(content, sign, getPublicKey());
    }

    @Autowired
    public void setPublicKey(@Value("${tranPublicKey}") String tranPublicKey) {
        this.publicKey = tranPublicKey;
    }

    @Autowired
    public void setPrivateKey(@Value("${privateKey}") String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @return the publicKey
     */
    public static String getPublicKey() {
        return publicKey;
    }

    /**
     * @return the privateKey
     */
    public static String getPrivateKey() {
        return privateKey;
    }
}
