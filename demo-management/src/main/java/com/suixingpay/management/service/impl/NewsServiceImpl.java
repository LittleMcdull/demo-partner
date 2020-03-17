/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.service.impl;

import com.suixingpay.core.domain.News;
import com.suixingpay.core.domain.NewsInfo;
import com.suixingpay.core.mapper.NewsInfoMapper;
import com.suixingpay.core.mapper.NewsMapper;
import com.suixingpay.core.utils.UuidUtil;
import com.suixingpay.management.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28 13:24
 * @version: V1.0
 */
@Service
@Slf4j
public class NewsServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private NewsInfoMapper newsInfoMapper;

    @Override
    public List<News> selectNewsList() {

        List<News> newsList = newsMapper.selectList();
        return newsList;
    }

    @Override
    public int selectNewsTotal() {
        return newsMapper.selectCount();
    }

    @Override
    public News selectNewsInfo(String infoId) {

        // 更新浏览次数
        News news = newsMapper.selectByInfoId(infoId);
        news.setViews(news.getViews() + 1);
        newsMapper.updateByPrimaryKeySelective(news);

        NewsInfo newsInfo = newsInfoMapper.selectByPrimaryKey(infoId);
        news.setNewsInfo(newsInfo);

        return news;
    }

    @Override
    public boolean insertNews(News news) {

        String newsInfoId = UuidUtil.getUuid();
        news.setId(UuidUtil.getUuid());
        news.setInfoId(newsInfoId);
        news.setViews(0);
        news.setCreateDate(new Date());
        int newsInsert = newsMapper.insertSelective(news);

        NewsInfo newsInfo = news.getNewsInfo();
        newsInfo.setId(newsInfoId);
        newsInsert += newsInfoMapper.insert(newsInfo);

        return newsInsert == 2;
    }

    @Override
    public int updateNews(News news) {
        news.setViews(news.getViews() + 1);
        return newsMapper.updateByPrimaryKeySelective(news);
    }
}
