package com.suixingpay.management.service.impl;

import com.suixingpay.core.domain.LearnResouce;
import com.suixingpay.management.service.LearnResourceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LearnResourceServiceImpl implements LearnResourceService {

    @Override
    public List<LearnResouce> getAll() {
        List<LearnResouce> learnList = new ArrayList<LearnResouce>();
        LearnResouce bean = new LearnResouce("官方参考文档", "Spring Boot Reference Guide",
                "http://docs.spring.io/spring-boot/docs/1.5.1.RELEASE/reference/htmlsingle/#getting-started-first-application",
                new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("官方SpriongBoot例子", "官方SpriongBoot例子",
                "https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples",
                new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("龙国学院", "Spring Boot 教程系列学习",
                "http://www.roncoo.com/article/detail/125488",
                new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("嘟嘟MD独立博客", "Spring Boot干货系列 ",
                "http://tengj.top/", new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("后端编程嘟", "Spring Boot教程和视频 ",
                "http://www.toutiao.com/m1559096720023553/", new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("程序猿DD", "Spring Boot系列", "http://www.roncoo.com/article/detail/125488", new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("纯洁的微笑", "Sping Boot系列文章", "http://www.ityouknow.com/spring-boot", new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("CSDN——小当博客专栏", "Sping Boot学习", "http://blog.csdn.net/column/details/spring-boot.html", new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("梁桂钊的博客", "Spring Boot 揭秘与实战", "http://blog.csdn.net/column/details/spring-boot.html", new Date(), Math.random() * 1000);
        learnList.add(bean);
        bean = new LearnResouce("林祥纤博客系列", "从零开始学Spring Boot ", "http://412887952-qq-com.iteye.com/category/356333", new Date(), Math.random() * 1000);
        learnList.add(bean);

        return learnList;
    }
}
