package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.YishengjiuzhenEntity;
import com.cl.entity.view.YishengjiuzhenView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 医生就诊
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface YishengjiuzhenDao extends BaseMapper<YishengjiuzhenEntity> {

    List<YishengjiuzhenView> selectListView(@Param("ew") Wrapper<YishengjiuzhenEntity> wrapper);

    List<YishengjiuzhenView> selectListView(Pagination page, @Param("ew") Wrapper<YishengjiuzhenEntity> wrapper);

    YishengjiuzhenView selectView(@Param("ew") Wrapper<YishengjiuzhenEntity> wrapper);


}
