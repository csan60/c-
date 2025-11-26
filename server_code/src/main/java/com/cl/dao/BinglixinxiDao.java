package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.BinglixinxiEntity;
import com.cl.entity.view.BinglixinxiView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 病历信息
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface BinglixinxiDao extends BaseMapper<BinglixinxiEntity> {

    List<BinglixinxiView> selectListView(@Param("ew") Wrapper<BinglixinxiEntity> wrapper);

    List<BinglixinxiView> selectListView(Pagination page, @Param("ew") Wrapper<BinglixinxiEntity> wrapper);

    BinglixinxiView selectView(@Param("ew") Wrapper<BinglixinxiEntity> wrapper);


}
