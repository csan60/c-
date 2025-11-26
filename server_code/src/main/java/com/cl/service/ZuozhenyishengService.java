package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.ZuozhenyishengEntity;
import com.cl.entity.view.ZuozhenyishengView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 坐诊医生
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface ZuozhenyishengService extends IService<ZuozhenyishengEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<ZuozhenyishengView> selectListView(Wrapper<ZuozhenyishengEntity> wrapper);

    ZuozhenyishengView selectView(@Param("ew") Wrapper<ZuozhenyishengEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<ZuozhenyishengEntity> wrapper);


}

