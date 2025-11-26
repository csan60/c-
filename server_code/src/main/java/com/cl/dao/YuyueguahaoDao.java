package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.YuyueguahaoEntity;
import com.cl.entity.view.YuyueguahaoView;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 预约挂号
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface YuyueguahaoDao extends BaseMapper<YuyueguahaoEntity> {

    List<YuyueguahaoView> selectListView(@Param("ew") Wrapper<YuyueguahaoEntity> wrapper);

    List<YuyueguahaoView> selectListView(Pagination page, @Param("ew") Wrapper<YuyueguahaoEntity> wrapper);

    YuyueguahaoView selectView(@Param("ew") Wrapper<YuyueguahaoEntity> wrapper);


    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<YuyueguahaoEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<YuyueguahaoEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<YuyueguahaoEntity> wrapper);


}
