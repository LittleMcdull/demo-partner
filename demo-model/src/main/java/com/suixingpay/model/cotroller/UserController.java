package com.suixingpay.model.cotroller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @ApiOperation(value = "接口的功能介绍",notes = "提示接口使用者注意事项")
    @GetMapping(value = "/demo")
    public String index(String name) {

        return "hello "+ name;
    }
}