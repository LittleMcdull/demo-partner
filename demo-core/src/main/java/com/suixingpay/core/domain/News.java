/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/21
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.core.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/21 17:03
 * @version: V1.0
 */
@Data
@ApiModel("新闻实体类")
public class News {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("新闻标题")
    private String title;

    @ApiModelProperty("浏览次数")
    private int views;

    @ApiModelProperty("详情ID")
    private String infoId;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("修改时间")
    private Date updateDate;

    @ApiModelProperty("发表人")
    private String createBy;

    @ApiModelProperty("新闻状态")
    private String newsStatus;

    @ApiModelProperty("新闻正文")
    private NewsInfo newsInfo;
}