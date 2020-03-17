/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 2:55 PM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.feignclient;

import com.suixingpay.api.controller.NewsApi;
import com.suixingpay.api.domain.request.RequestRpc;
import com.suixingpay.api.domain.response.ResponseRpc;
import com.suixingpay.core.domain.News;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 2:55 PM
 */
@FeignClient(value = "demo-management", path = "/demo/management/newsApi")
public interface NewsApiFeignClient {

    /**
     * 发表评论，然后开始修改新闻的阅读次数
     *
     * @param requestRpc
     * @return
     */
    @PostMapping("/updateReader")
    ResponseRpc<String> updateNewsReader(@RequestBody RequestRpc<News> requestRpc);
}
