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
@ApiModel("评论实体类")
public class CommentInfo {

    @ApiModelProperty("评论ID")
    private String id;

    @ApiModelProperty("评论人")
    private String userId;

    @ApiModelProperty("评论新闻ID")
    private String newsId;

    @ApiModelProperty("评论内容")
    private String comment;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("上一条评论ID")
    private String commentId;

    @ApiModelProperty("评论删除状态")
    private String commentStatus;

    private String userName;
}