package com.cl.dao;

import com.cl.entity.ChatMessage;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.ChatMessageView;


/**
 * 聊天消息
 * 
 * @author 
 * @email 
 * @date 2024-01-01 10:00:00
 */
public interface ChatMessageDao extends BaseMapper<ChatMessage> {
	
	List<ChatMessageView> selectListView(@Param("ew") Wrapper<ChatMessage> wrapper);

	List<ChatMessageView> selectListView(Pagination page,@Param("ew") Wrapper<ChatMessage> wrapper);
	
	ChatMessageView selectView(@Param("ew") Wrapper<ChatMessage> wrapper);
	

}