/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28
 * @Copyright: ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.management.service;

import com.suixingpay.core.domain.News;

import java.util.List;

/**
 * @Description:
 * @author: zhouchen[zhou_chen1@suixingpay.com]
 * @date: 2019/09/28 13:23
 * @version: V1.0
 */
public interface NewsService {

    List<News> selectNewsList();

    int selectNewsTotal();

    News selectNewsInfo(String infoId);

    boolean insertNews(News news);

    int updateNews(News news);
}
