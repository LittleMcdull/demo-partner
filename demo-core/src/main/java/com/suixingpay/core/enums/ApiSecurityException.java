package com.suixingpay.core.enums;

/**
 * @author: zhou_chen[zhou_chen1@suixingpay.com]
 * @date: 2020/3/16 5:53 PM
 */
public class ApiSecurityException extends RuntimeException  {

    private static final long serialVersionUID = 346834750473575147L;

    /**
     * 异常码
     */
    private String code;

    /**
     * 异常信息
     */
    private String message;

    private String reqId;

    /**
     * 构造器
     */
    public ApiSecurityException() {
        super();
    }

    /**
     * 构造器
     */
    public ApiSecurityException(CommonExceptionEnum exceptionEnum,String reqId) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
        this.reqId = reqId;
    }

    /**
     * 构造器
     */
    public ApiSecurityException(ApiSecurityException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
        this.reqId = e.getReqId();
    }

    /**
     * 构造器
     *
     * @param message
     */
    public ApiSecurityException(String message) {
        super(message);
        message = message;
    }

    /**
     * 构造器
     *
     * @param code
     * @param message
     */
    public ApiSecurityException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    /**
     * @return the reqId
     */
    public String getReqId() {
        return reqId;
    }

    /**
     * @param reqId the reqId to set
     */
    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getCode() {
        return  code;
    }
}