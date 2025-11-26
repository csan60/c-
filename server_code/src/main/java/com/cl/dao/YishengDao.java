package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.YishengEntity;
import com.cl.entity.view.YishengView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 医生
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface YishengDao extends BaseMapper<YishengEntity> {

    List<YishengView> selectListView(@Param("ew") Wrapper<YishengEntity> wrapper);

    List<YishengView> selectListView(Pagination page, @Param("ew") Wrapper<YishengEntity> wrapper);

    YishengView selectView(@Param("ew") Wrapper<YishengEntity> wrapper);


}
