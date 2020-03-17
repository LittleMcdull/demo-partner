/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 4:37 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.utils.sign;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * RSA签名验签类
 *
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 4:37 PM
 */
@Slf4j
public class RSASignature {

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static final String UTF8 = "UTF-8";


    public static String signStr(String reqStr, String privateKey) {

        HashMap<String, Object> reqMap=  JSON.parseObject(reqStr,LinkedHashMap.class,Feature.OrderedField);
        String signContent = RSASignature.getOrderContent(reqMap);
        log.info("待加签字符串:{}", signContent);
        String sign = RSASignature.sign(signContent, privateKey, UTF8);
        log.info("加签后签名: {}", sign);

        return sign;
    }

    /**
     * 针对参数进行排序拼装
     *
     * @param requestParam
     * @return
     */
    public static String getOrderContent(Map<String, Object> requestParam) {
        Map<String, Object> sortedParams = new TreeMap<String, Object>();
        if ((requestParam != null) && (requestParam.size() > 0)) {
            sortedParams.putAll(requestParam);
        }
        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = sortedParams.get(key);
            if (StringUtils.isNotBlank(key)&& value!=null) {
                content.append((index == 0 ? "" : "&") + key + "=" + value);
                index++;
            }
        }
        return content.toString();
    }


    /**
     * RSA签名
     *
     * @param content    待签名数据
     * @param privateKey 商户私钥
     * @param encode     字符集编码
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String encode) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(decryptBASE64(privateKey));

            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(encode));

            byte[] signed = signature.sign();

            return new String(encryptBASE64(signed));
        } catch (Exception e) {
            log.error("签名失败：{}",e.getMessage());
            return null;
        }

    }

    /**
     * rsa 签名
     * @param content 待签名内容
     * @param privateKey  私钥
     * @return
     */
    public static byte[] sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(decryptBASE64(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(UTF8));
            byte[] signed = signature.sign();
            return signed;
        } catch (Exception e) {
            log.error("签名失败：{}",e.getMessage());
            return null;
        }
    }

    /**
     * RSA验签名检查
     *
     * @param content   待签名数据
     * @param sign      签名值
     * @param publicKey 分配给开发商公钥
     * @param encode    字符集编码
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey, String encode) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = decryptBASE64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(encode));

            boolean bverify = signature.verify(decryptBASE64(sign));
            return bverify;

        } catch (Exception e) {
            log.error("验签失败：{}",e.getMessage());
            return false;
        }
    }

    /**
     * RSA验签名检查
     * @param content 待签名数据
     * @param sign 签名值
     * @param publicKey 分配给开发商公钥
     * @return 布尔值
     */
    public static boolean doCheck(String content, String sign, String publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = decryptBASE64(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(UTF8));

            boolean bverify = signature.verify(decryptBASE64(sign));
            return bverify;

        } catch (Exception e) {
            log.error("验签失败：{}",e.getMessage());
            return false;
        }
    }

    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encode(key).replaceAll("[\\s*\t\n\r]", "");
    }

    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }
}
