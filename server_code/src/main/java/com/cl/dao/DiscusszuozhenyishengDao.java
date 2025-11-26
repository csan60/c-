package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.DiscusszuozhenyishengEntity;
import com.cl.entity.view.DiscusszuozhenyishengView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 坐诊医生评论表
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:21
 */
public interface DiscusszuozhenyishengDao extends BaseMapper<DiscusszuozhenyishengEntity> {

    List<DiscusszuozhenyishengView> selectListView(@Param("ew") Wrapper<DiscusszuozhenyishengEntity> wrapper);

    List<DiscusszuozhenyishengView> selectListView(Pagination page, @Param("ew") Wrapper<DiscusszuozhenyishengEntity> wrapper);

    DiscusszuozhenyishengView selectView(@Param("ew") Wrapper<DiscusszuozhenyishengEntity> wrapper);


}
