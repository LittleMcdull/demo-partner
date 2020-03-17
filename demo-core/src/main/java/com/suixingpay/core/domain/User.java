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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/21 17:03
 * @version: V1.0
 */
@Data
@ApiModel("用户实体类")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("角色ID")
    private String ruleId;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("更新时间")
    private Date updateDate;

    @ApiModelProperty("使用状态")
    private String userStatus;
}
