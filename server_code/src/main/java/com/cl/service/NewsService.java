package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.entity.NewsEntity;
import com.cl.entity.view.NewsView;
import com.cl.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 公告信息
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface NewsService extends IService<NewsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<NewsView> selectListView(Wrapper<NewsEntity> wrapper);

    NewsView selectView(@Param("ew") Wrapper<NewsEntity> wrapper);

    PageUtils queryPage(Map<String, Object> params, Wrapper<NewsEntity> wrapper);


}

