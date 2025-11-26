package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.YishengjiuzhenEntity;
import com.cl.entity.view.YishengjiuzhenView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 医生就诊
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface YishengjiuzhenService extends IService<YishengjiuzhenEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<YishengjiuzhenView> selectListView(Wrapper<YishengjiuzhenEntity> wrapper);

    YishengjiuzhenView selectView(@Param("ew") Wrapper<YishengjiuzhenEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<YishengjiuzhenEntity> wrapper);


}

