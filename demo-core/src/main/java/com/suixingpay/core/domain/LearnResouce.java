package com.suixingpay.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * LearnResouce 实体类
 *
 * @author zhouchen
 * @date 108-10-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LearnResouce {
    /**
     * 作者 哈哈哈哈
     */
    private String author;
    /**
     * 教程名称
     */
    private String title;

    /**
     * 教程地址
     */
    private String url;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 浏览次数
     */
    private double views;
}
