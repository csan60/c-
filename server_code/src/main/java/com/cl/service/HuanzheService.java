package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.HuanzheEntity;
import com.cl.entity.view.HuanzheView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 患者
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface HuanzheService extends IService<HuanzheEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<HuanzheView> selectListView(Wrapper<HuanzheEntity> wrapper);

    HuanzheView selectView(@Param("ew") Wrapper<HuanzheEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<HuanzheEntity> wrapper);


    List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<HuanzheEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<HuanzheEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<HuanzheEntity> wrapper);


}

