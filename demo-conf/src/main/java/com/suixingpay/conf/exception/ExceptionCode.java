package com.suixingpay.conf.exception;

/**
 * 异常类型
 * 
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月17日 上午12:01:34
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月17日 上午12:01:34
 */
public enum ExceptionCode implements IExceptionCode {
    /**
     *
     */
    NOT_MODIFIED(304, "未修改"),
    /**
     * 请求出现错误，比如请求头不对、参数验证失败等，所有不想明确区分的客户端请求出错都可以返回 400。
     */
    BAD_REQUEST(400, "请求参数有误,比如请求头不对、参数验证失败等"),
    /**
     * 没有提供认证信息。比如说，请求的时候没有带上 Token 等。
     */
    UNAUTHORIZED(401, "未登录或登录已过期"),
    /**
     * 请求的资源不允许访问。比如说，你使用普通用户的 Token 去请求管理员才能访问的资源。
     */
    FORBIDDEN(403, "没有权限"),
    /**
     * 请求的内容不存在。
     */
    NOT_FOUND(404, "未找到记录"),
    /**
     * 业务处理错误
     */
    INTERNAL_SERVER_ERROR(500, "业务处理错误"),
    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用");

    private final int value;

    private final String defaultMessage;

    ExceptionCode(int value, String defaultMessage) {
        this.value = value;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }

    /**
     *
     * @param value
     * @return
     */
    public static ExceptionCode getValue(Integer value) {
        if (null == value) {
            return null;
        }
        for (ExceptionCode item : ExceptionCode.values()) {
            if (item.value == value.intValue()) {
                return item;
            }
        }
        return null;
    }

}