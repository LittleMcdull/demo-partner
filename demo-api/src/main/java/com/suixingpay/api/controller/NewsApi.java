/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 周晨<zhou_chen1 @ suixingpay.com>
 * @date: 2020/3/16 10:59 AM
 * @Copyright: ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.api.controller;

import com.suixingpay.api.domain.request.RequestRpc;
import com.suixingpay.api.domain.response.ResponseRpc;
import com.suixingpay.core.domain.News;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: 周晨<zhou_chen1@suixingpay.com>
 * @date: 2020/3/16 10:59 AM
 */
@RequestMapping("/newsApi")
@Api(description = "新闻API接口")
public interface NewsApi {

    /**
     * 发表评论，然后开始修改新闻的阅读次数
     *
     * @param requestRpc
     * @return
     */
    @PostMapping("/updateReader")
    ResponseRpc<String> updateNewsReader(@RequestBody RequestRpc<News> requestRpc);

    /**
     * 删除新闻之后, 删除相对应的评论
     *
     * @param requestRpc
     * @return
     */
    @PostMapping("/deleteReader")
    ResponseRpc<String> deleteNewsReader(@RequestBody RequestRpc<News> requestRpc);
}
