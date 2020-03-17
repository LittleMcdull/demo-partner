/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.controller;

import com.suixingpay.core.domain.News;
import com.suixingpay.core.domain.response.ResponseBean;
import com.suixingpay.management.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28 13:31
 * @version: V1.0
 */
@RestController
@RequestMapping("/news")
@CrossOrigin
@Slf4j
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/list")
    public ResponseBean list(){

        return ResponseBean.list(newsService.selectNewsTotal(),
                newsService.selectNewsList());
    }

    @GetMapping("/info/{infoId}")
    public ResponseBean info(@PathVariable("infoId") String infoId){
        return ResponseBean.ok(newsService.selectNewsInfo(infoId));
    }

    @PostMapping("/add")
    public ResponseBean add(@RequestBody News news){
        if (newsService.insertNews(news)) {
            return ResponseBean.ok("添加新闻成功");
        }

        return ResponseBean.error("添加新闻失败");
    }


}
