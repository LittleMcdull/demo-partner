/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.controller;

import com.suixingpay.core.domain.CommentInfo;
import com.suixingpay.core.bean.ResponseMessage;
import com.suixingpay.management.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28 13:40
 * @version: V1.0
 */
@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/comment")
@Api(description = "评论管理")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/list/{newsId}")
    @ApiOperation(value = "评论管理中心", notes = "评论管理中心")
    public ResponseMessage list(@PathVariable("newsId") String newsId){
        return ResponseMessage.ok(commentService.selectComment(newsId));
    }

    @PostMapping("/insert")
    @ApiOperation(value = "新增评论信息", notes = "新增评论信息")
    public ResponseMessage comment(@RequestBody CommentInfo commentInfo){
        if (commentService.insetComment(commentInfo)) {
            return ResponseMessage.ok("评论成功");
        }

        return ResponseMessage.error("评论失败");
    }
}
