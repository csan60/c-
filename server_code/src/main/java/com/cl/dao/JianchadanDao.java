package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.JianchadanEntity;
import com.cl.entity.view.JianchadanView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 检查单
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface JianchadanDao extends BaseMapper<JianchadanEntity> {

    List<JianchadanView> selectListView(@Param("ew") Wrapper<JianchadanEntity> wrapper);

    List<JianchadanView> selectListView(Pagination page, @Param("ew") Wrapper<JianchadanEntity> wrapper);

    JianchadanView selectView(@Param("ew") Wrapper<JianchadanEntity> wrapper);


}
