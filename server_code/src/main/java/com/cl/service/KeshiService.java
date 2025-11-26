package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.KeshiEntity;
import com.cl.entity.view.KeshiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 科室
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface KeshiService extends IService<KeshiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<KeshiView> selectListView(Wrapper<KeshiEntity> wrapper);

    KeshiView selectView(@Param("ew") Wrapper<KeshiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<KeshiEntity> wrapper);


}

