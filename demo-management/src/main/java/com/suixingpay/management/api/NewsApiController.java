/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 1:51 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.api;

import com.suixingpay.api.controller.NewsApi;
import com.suixingpay.api.domain.request.RequestRpc;
import com.suixingpay.api.domain.response.ResponseRpc;
import com.suixingpay.core.annotations.ApiSecurity;
import com.suixingpay.core.domain.News;
import com.suixingpay.management.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.suixingpay.api.domain.response.ResponseRpc.error;
import static com.suixingpay.api.domain.response.ResponseRpc.ok;

/**
 * @author: 周晨<zhou_chen1@suixingpay.com>
 * @date: 2020/3/16 1:51 PM
 */
@Slf4j
@RestController
public class NewsApiController implements NewsApi {

    @Autowired
    private NewsService newsService;

    @Override
    @ApiSecurity
    public ResponseRpc<String> updateNewsReader(@RequestBody RequestRpc<News> requestRpc) {
        log.info("|发表评论|修改新闻的阅读次数|请求参数|[{}]", requestRpc.toString());
        News news = requestRpc.getReqData();
        News updateNews = new News();

        // 修改操作
        updateNews.setId(news.getId());
        updateNews.setViews(news.getViews());
        int result = newsService.updateNews(updateNews);

        if (result > 0 ) {
            return ok("同步阅读次数成功.");
        }
        return error("同步阅读次数成功.");
    }

    @Override
    public ResponseRpc<String> deleteNewsReader(@RequestBody RequestRpc<News> requestRpc) {
        return null;
    }
}
