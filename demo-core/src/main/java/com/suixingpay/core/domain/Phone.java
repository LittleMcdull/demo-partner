package com.suixingpay.core.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("手机实体类")
public class Phone {

    private String id;


    private BigDecimal oldPrice;


    private BigDecimal newPrice;


    private String imgUrl;


    private Date createDate;


    private Date updateDate;


    private String title;
}