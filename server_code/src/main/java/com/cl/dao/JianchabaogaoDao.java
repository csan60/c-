package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.JianchabaogaoEntity;
import com.cl.entity.view.JianchabaogaoView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 检查报告
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface JianchabaogaoDao extends BaseMapper<JianchabaogaoEntity> {

    List<JianchabaogaoView> selectListView(@Param("ew") Wrapper<JianchabaogaoEntity> wrapper);

    List<JianchabaogaoView> selectListView(Pagination page, @Param("ew") Wrapper<JianchabaogaoEntity> wrapper);

    JianchabaogaoView selectView(@Param("ew") Wrapper<JianchabaogaoEntity> wrapper);


}
