package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.YuyueguahaoEntity;
import com.cl.entity.view.YuyueguahaoView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 预约挂号
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface YuyueguahaoService extends IService<YuyueguahaoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<YuyueguahaoView> selectListView(Wrapper<YuyueguahaoEntity> wrapper);

    YuyueguahaoView selectView(@Param("ew") Wrapper<YuyueguahaoEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<YuyueguahaoEntity> wrapper);


    List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<YuyueguahaoEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<YuyueguahaoEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<YuyueguahaoEntity> wrapper);


}

