/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/25
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.controller;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.core.domain.User;
import com.suixingpay.core.bean.ResponseMessage;
import com.suixingpay.management.service.IndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/25 18:16
 * @version: V1.0
 */
@RestController
@RequestMapping("/index")
@CrossOrigin
@Slf4j
@Api(description = "首页登陆注册轮播图")
public class IndexController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/login")
    @ApiOperation(value = "登陆首页", notes = "登陆首页")
    public ResponseMessage login(@RequestBody User user){

        log.info("|用户登录开始|登录请求参数|[{}]", JSONObject.toJSONString(user));

        User loginUser = indexService.selectUser(user.getUserName(), user.getPassword());
        if (null == loginUser){
            return ResponseMessage.error("用户名或密码不正确.");
        }

        return ResponseMessage.ok("验证登录成功.");
    }

    @PostMapping("/register")
    @ApiOperation(value = "注册用户", notes = "注册用户")
    public ResponseMessage register(@RequestBody User user){

        log.info("|用户注册开始|登录注册参数|[{}]", JSONObject.toJSONString(user));

        if (!indexService.insertUser(user)){
            return ResponseMessage.error("用户注册失败.");
        }

        return ResponseMessage.ok("用户注册成功.");
    }

    @GetMapping("/img")
    @ApiOperation(value = "首页轮播图", notes = "首页轮播图")
    public ResponseMessage img(){

        List<String> imgList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String path = "http://39.108.236.167:8080/demo-management/images/"+i+".jpg";
            imgList.add(path);
        }
        return ResponseMessage.ok(imgList);
    }
}

