/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/25
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/25 18:08
 * @version: V1.0
 */
public class UuidUtil {

    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-", StringUtils.EMPTY);
    }

    /**
     * 生成18位uuids
     * @return
     */
    public static String get18Uuid(){
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {
            //有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        String uuid="OPEN"+String.format("%014d", hashCodeV);
        return uuid;
    }

    /**
     *
     * 获取64位主键
     * @return
     */
    public static String get64Uuid() {
        // 15位数字数组
        int[] number = new int[15];
        // 循环变量
        int i = 0;
        StringBuffer bussinessNo = new StringBuffer(64);
        bussinessNo.append(getStringDate());
        bussinessNo.append(getUuid());
        // 生成15位随机数算法
        for (i = 0; i < number.length; i++) {
            if (number[i] == 0) {
                // 产生0-10之间的随机小数，强制转换成正数
                number[i] = (int) (Math.random() * 10);
            }
            bussinessNo.append(number[i]);
        }
        return bussinessNo.toString();
    }

    /**
     *
     * 获取现在时间 yyyyMMddHHmmssSS(17位)
     * @return
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}
