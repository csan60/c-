package com.cl.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.cl.entity.ZuozhenyishengEntity;
import com.cl.entity.view.ZuozhenyishengView;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 坐诊医生
 *
 * @author
 * @email
 * @date 2025-02-28 19:06:20
 */
public interface ZuozhenyishengDao extends BaseMapper<ZuozhenyishengEntity> {

    List<ZuozhenyishengView> selectListView(@Param("ew") Wrapper<ZuozhenyishengEntity> wrapper);

    List<ZuozhenyishengView> selectListView(Pagination page, @Param("ew") Wrapper<ZuozhenyishengEntity> wrapper);

    ZuozhenyishengView selectView(@Param("ew") Wrapper<ZuozhenyishengEntity> wrapper);


}
