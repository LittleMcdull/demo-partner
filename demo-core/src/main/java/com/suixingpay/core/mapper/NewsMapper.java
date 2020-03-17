package com.suixingpay.core.mapper;

import com.suixingpay.core.domain.News;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsMapper {

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteByPrimaryKey(String id);

    /**
     * 插入
     * @param record
     * @return
     */
    int insert(News record);

    /**
     * 插入
     * @param record
     * @return
     */
    int insertSelective(News record);

    /**
     * 查询
     * @param id
     * @return
     */
    News selectByPrimaryKey(String id);

    /**
     * 修改
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(News record);

    /**
     * 修改
     * @param record
     * @return
     */
    int updateByPrimaryKey(News record);

    /**
     * 查询新闻列表
     * @return
     */
    List<News> selectList();

    /**
     * 统计条数
     * @return
     */
    int selectCount();

    /**
     * 查询
     * @param infoId
     * @return
     */
    News selectByInfoId(String infoId);
}