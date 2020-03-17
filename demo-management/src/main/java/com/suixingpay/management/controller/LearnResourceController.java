package com.suixingpay.management.controller;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.core.domain.LearnResouce;
import com.suixingpay.core.bean.ResponseMessage;
import com.suixingpay.management.service.LearnResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资源访问controller
 *
 * @author zhouchen
 * @date 2018-10-25
 */
@CrossOrigin
@RestController
@RequestMapping("/learn")
public class LearnResourceController {

    @Autowired
    private LearnResourceService learnResourceService;

    @GetMapping("/book")
    public ResponseMessage index() {
        JSONObject result = new JSONObject();
        List<LearnResouce> learnList = learnResourceService.getAll();
        return ResponseMessage.ok(learnList);
    }
}