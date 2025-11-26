package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.BinglixinxiEntity;
import com.cl.entity.view.BinglixinxiView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 病历信息
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface BinglixinxiService extends IService<BinglixinxiEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<BinglixinxiView> selectListView(Wrapper<BinglixinxiEntity> wrapper);

    BinglixinxiView selectView(@Param("ew") Wrapper<BinglixinxiEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<BinglixinxiEntity> wrapper);


}

