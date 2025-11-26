package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.MedicalPlan;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 医嘱计划
 *
 * @author
 * @email
 * @date 2025-02-28
 */
public interface MedicalPlanDao extends BaseMapper<MedicalPlan> {

    List<MedicalPlan> selectListView(@Param("ew") Wrapper<MedicalPlan> wrapper);

    List<MedicalPlan> selectListView(Pagination page, @Param("ew") Wrapper<MedicalPlan> wrapper);

    MedicalPlan selectView(@Param("ew") Wrapper<MedicalPlan> wrapper);

    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<MedicalPlan> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<MedicalPlan> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params, @Param("ew") Wrapper<MedicalPlan> wrapper);
}