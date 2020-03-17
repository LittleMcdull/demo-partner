package com.suixingpay.core.mapper;

import com.suixingpay.core.domain.CommentInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentInfoMapper {

    /**
     * 根据ID删除
     * @param id ID
     * @return
     */
    int deleteByPrimaryKey(String id);

    /**
     * 新增评论
     * @param record
     * @return
     */
    int insert(CommentInfo record);

    /**
     * 按条件新增评论
     * @param record
     * @return
     */
    int insertSelective(CommentInfo record);

    /**
     * 查询评论
     * @param id
     * @return
     */
    CommentInfo selectByPrimaryKey(@Param("id") String id);

    /**
     * 根据主键和存在条件修改评论
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(CommentInfo record);


    /**
     * 修改品论内容
     * @param record
     * @return
     */
    int updateByPrimaryKey(CommentInfo record);

    /**
     * 查询新闻列表下的评论
     * @param newsId
     * @return
     */
    List<CommentInfo> selectByNewsId(@Param("newsId") String newsId);
}