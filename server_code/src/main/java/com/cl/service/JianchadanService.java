package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.JianchadanEntity;
import com.cl.entity.view.JianchadanView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 检查单
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface JianchadanService extends IService<JianchadanEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<JianchadanView> selectListView(Wrapper<JianchadanEntity> wrapper);

    JianchadanView selectView(@Param("ew") Wrapper<JianchadanEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<JianchadanEntity> wrapper);


}

