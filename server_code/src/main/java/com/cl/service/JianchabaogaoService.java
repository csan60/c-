package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.JianchabaogaoEntity;
import com.cl.entity.view.JianchabaogaoView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 检查报告
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface JianchabaogaoService extends IService<JianchabaogaoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<JianchabaogaoView> selectListView(Wrapper<JianchabaogaoEntity> wrapper);

    JianchabaogaoView selectView(@Param("ew") Wrapper<JianchabaogaoEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<JianchabaogaoEntity> wrapper);


}

