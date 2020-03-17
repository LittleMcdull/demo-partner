/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.api.domain.request.RequestRpc;
import com.suixingpay.api.domain.response.ResponseRpc;
import com.suixingpay.core.advice.KeyProperties;
import com.suixingpay.core.domain.CommentInfo;
import com.suixingpay.core.domain.News;
import com.suixingpay.core.mapper.CommentInfoMapper;
import com.suixingpay.core.mapper.NewsMapper;
import com.suixingpay.core.utils.UuidUtil;
import com.suixingpay.core.utils.sign.RSASignature;
import com.suixingpay.management.constants.BeanConvert;
import com.suixingpay.management.feignclient.NewsApiFeignClient;
import com.suixingpay.management.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28 13:23
 * @version: V1.0
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private KeyProperties keyProperties;

    @Autowired
    private CommentInfoMapper commentInfoMapper;

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private NewsApiFeignClient newsApiFeignclient;

    @Override
    public List<CommentInfo> selectComment(String newsId) {
        return commentInfoMapper.selectByNewsId(newsId);
    }

    @Override
    public boolean insetComment(CommentInfo commentInfo) {
        commentInfo.setId(UuidUtil.getUuid());
        commentInfo.setCreateDate(new Date());
        int result = commentInfoMapper.insertSelective(commentInfo);
        if (result > 0) {
            log.info("|评论成功|修改阅读次数 +1 ｜新闻ID[{}]", commentInfo.getNewsId());
            RequestRpc<News> requestRpc = BeanConvert.buildRequest();
            News news = newsMapper.selectByPrimaryKey(commentInfo.getNewsId());

            News reqNews = new News();
            reqNews.setId(news.getId());
            reqNews.setViews(news.getViews());
            requestRpc.setReqData(reqNews);

            // 签名
            String reqStr = JSONObject.toJSONString(requestRpc);
            requestRpc.setSign(RSASignature.signStr(reqStr, keyProperties.getPrivateKey()));

            log.info("|评论成功|修改阅读次数|请求Cloud接口, 请求参数[{}]", JSONObject.toJSONString(requestRpc));
            ResponseRpc<String> responseRpc = newsApiFeignclient.updateNewsReader(requestRpc);

            if (StringUtils.equals(responseRpc.getCode(), "200")) {
                log.info("|评论成功|修改阅读次数成功");
            }
            return true;
        }
        return  false;
    }
}
