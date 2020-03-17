/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 11:56 AM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.enums;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 11:56 AM
 */
public enum CommonExceptionEnum {
    
    /**
     * 操作成功
     */
    SUCCESS("0000", "操作成功"),

    /**
     * 基础异常
     */
    SYS_EXCEPTION("0001", "系统异常"),

    /**
     * 报文格式错误
     */
    VALIDATE_EXCEPTION("0002", "参数错误"),

    /**
     * 新增数据异常
     */
    SXF0003("0003", "机构id异常"),

    /**
     * 解密失败
     */
    SXF0004("0004", "密钥错误"),

    /**
     * 删除数据异常
     */
    SXF0005("0005", "验签失败"),

    /**
     * 查询数据异常
     */
    SXF0006("0006", "该功能未开通"),

    /**
     * 版本错误
     */
    VERSION_EXCEPTION("0007", "版本错误"),

    ;


    /**
     * 构造方法
     *
     * @param code
     * @param message
     */
    CommonExceptionEnum(String code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    /**
     * 通过code获取枚举
     *
     * @param code
     * @return
     */
    public static CommonExceptionEnum valueOfEnum(String code) {
        CommonExceptionEnum[] types = values();
        for (CommonExceptionEnum type : types) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }

    /**
     * code
     */
    private String code;

    /**
     * message
     */
    private String message;

    /**
     * 获取code
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置code
     *
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取message
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置message
     *
     * @return
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
