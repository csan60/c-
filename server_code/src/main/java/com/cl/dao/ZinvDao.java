package com.cl.dao;

import com.cl.entity.ZinvEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.ZinvView;


/**
 * 子女用户
 * 
 * @author 
 * @email 
 * @date 2024-01-01 10:00:00
 */
public interface ZinvDao extends BaseMapper<ZinvEntity> {
	
	List<ZinvView> selectListView(@Param("ew") Wrapper<ZinvEntity> wrapper);

	List<ZinvView> selectListView(Pagination page,@Param("ew") Wrapper<ZinvEntity> wrapper);
	
	ZinvView selectView(@Param("ew") Wrapper<ZinvEntity> wrapper);
	

}