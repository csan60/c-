package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.YiyuanxinxiEntity;
import com.cl.entity.view.YiyuanxinxiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 医院信息
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface YiyuanxinxiService extends IService<YiyuanxinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<YiyuanxinxiView> selectListView(Wrapper<YiyuanxinxiEntity> wrapper);

    YiyuanxinxiView selectView(@Param("ew") Wrapper<YiyuanxinxiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<YiyuanxinxiEntity> wrapper);


}

