package com.suixingpay.conf.rabbitmq.destinations;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求追踪号生成器
 */
@Component
public interface ITraceNumManager {

    String DEFAULT_TRADE_NUM = "tradenum";

    String CONCAT_CHAR = "-";

    /**
     * 根据请求生成traceNum
     *
     * @param request
     * @return
     */
    String gen(HttpServletRequest request);

    /**
     * 根据前缀生成随机的traceNum
     *
     * @param prefix
     * @return
     */
    String gen(String prefix);

    /**
     * 获取已经设置的traceNum
     *
     * @return
     */
    String getTraceNum();

    /**
     * 开始追踪
     *
     * @param traceNum
     */
    void beginTrace(String traceNum);

    /**
     * 结束追踪
     */
    void endTrace();
}