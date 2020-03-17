package com.suixingpay.conf.exception;

/**
 * 错误码
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2018年1月19日 下午1:11:04
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2018年1月19日 下午1:11:04
 */
public interface IExceptionCode {
    /**
     *
     * @return
     */
    int value();

    /**
     *
     * @return
     */
    String defaultMessage();
}