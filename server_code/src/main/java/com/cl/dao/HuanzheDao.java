package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.HuanzheEntity;
import com.cl.entity.view.HuanzheView;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 患者
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface HuanzheDao extends BaseMapper<HuanzheEntity> {

    List<HuanzheView> selectListView(@Param("ew") Wrapper<HuanzheEntity> wrapper);

    List<HuanzheView> selectListView(Pagination page, @Param("ew") Wrapper<HuanzheEntity> wrapper);

    HuanzheView selectView(@Param("ew") Wrapper<HuanzheEntity> wrapper);


    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<HuanzheEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<HuanzheEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<HuanzheEntity> wrapper);


}
