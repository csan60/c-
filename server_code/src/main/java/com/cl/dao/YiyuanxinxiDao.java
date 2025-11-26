package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.YiyuanxinxiEntity;
import com.cl.entity.view.YiyuanxinxiView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 医院信息
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface YiyuanxinxiDao extends BaseMapper<YiyuanxinxiEntity> {

    List<YiyuanxinxiView> selectListView(@Param("ew") Wrapper<YiyuanxinxiEntity> wrapper);

    List<YiyuanxinxiView> selectListView(Pagination page, @Param("ew") Wrapper<YiyuanxinxiEntity> wrapper);

    YiyuanxinxiView selectView(@Param("ew") Wrapper<YiyuanxinxiEntity> wrapper);


}
