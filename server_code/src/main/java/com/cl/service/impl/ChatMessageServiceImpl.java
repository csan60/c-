package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.dao.ChatMessageDao;
import com.cl.entity.ChatMessage;
import com.cl.entity.view.ChatMessageView;
import com.cl.service.ChatMessageService;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("chatMessageService")
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageDao, ChatMessage> implements ChatMessageService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ChatMessage> page = this.selectPage(
                new Query<ChatMessage>(params).getPage(),
                new EntityWrapper<ChatMessage>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<ChatMessage> wrapper) {
        Page<ChatMessageView> page = new Query<ChatMessageView>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        return new PageUtils(page);
    }

    @Override
    public List<ChatMessageView> selectListView(Wrapper<ChatMessage> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public ChatMessageView selectView(Wrapper<ChatMessage> wrapper) {
        return baseMapper.selectView(wrapper);
    }

    @Override
    public int markAsRead(Long receiverId, String receiverTable, Long senderId, String senderTable) {
        ChatMessage update = new ChatMessage();
        update.setIsRead(1);
        update.setReadTime(new Date());
        return this.baseMapper.update(update, new EntityWrapper<ChatMessage>()
                .eq("receiver_id", receiverId)
                .eq("receiver_table", receiverTable)
                .eq("sender_id", senderId)
                .eq("sender_table", senderTable)
                .eq("is_read", 0));
    }
}