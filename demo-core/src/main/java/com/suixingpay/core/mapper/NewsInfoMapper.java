package com.suixingpay.core.mapper;

import com.suixingpay.core.domain.NewsInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsInfoMapper {

    /**
     * 删除新闻
     * @param id
     * @return
     */
    int deleteByPrimaryKey(String id);

    /**
     * 插入新闻
     * @param record
     * @return
     */
    int insert(NewsInfo record);

    /**
     * 插入新闻
     * @param record
     * @return
     */
    int insertSelective(NewsInfo record);

    /**
     * 查询新闻
     * @param id
     * @return
     */
    NewsInfo selectByPrimaryKey(String id);

    /**
     * 修改新闻
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(NewsInfo record);

    /**
     * 修改新闻
     * @param record
     * @return
     */
    int updateByPrimaryKeyWithBLOBs(NewsInfo record);
}