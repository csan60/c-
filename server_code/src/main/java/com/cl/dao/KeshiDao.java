package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.KeshiEntity;
import com.cl.entity.view.KeshiView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 科室
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface KeshiDao extends BaseMapper<KeshiEntity> {

    List<KeshiView> selectListView(@Param("ew") Wrapper<KeshiEntity> wrapper);

    List<KeshiView> selectListView(Pagination page, @Param("ew") Wrapper<KeshiEntity> wrapper);

    KeshiView selectView(@Param("ew") Wrapper<KeshiEntity> wrapper);


}
