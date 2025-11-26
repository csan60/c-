package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.DiscusszuozhenyishengEntity;
import com.cl.entity.view.DiscusszuozhenyishengView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 坐诊医生评论表
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface DiscusszuozhenyishengService extends IService<DiscusszuozhenyishengEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<DiscusszuozhenyishengView> selectListView(Wrapper<DiscusszuozhenyishengEntity> wrapper);

    DiscusszuozhenyishengView selectView(@Param("ew") Wrapper<DiscusszuozhenyishengEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<DiscusszuozhenyishengEntity> wrapper);


}

