package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.PaibanxinxiEntity;
import com.cl.entity.view.PaibanxinxiView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 排班信息
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface PaibanxinxiDao extends BaseMapper<PaibanxinxiEntity> {

    List<PaibanxinxiView> selectListView(@Param("ew") Wrapper<PaibanxinxiEntity> wrapper);

    List<PaibanxinxiView> selectListView(Pagination page, @Param("ew") Wrapper<PaibanxinxiEntity> wrapper);

    PaibanxinxiView selectView(@Param("ew") Wrapper<PaibanxinxiEntity> wrapper);


}
