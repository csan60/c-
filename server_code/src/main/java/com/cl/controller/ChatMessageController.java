package com.cl.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.entity.ChatMessage;
import com.cl.service.ChatMessageService;
import com.cl.utils.MPUtil;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 聊天消息
 * 后端接口
 */
@RestController
@RequestMapping("/chatMessage")
public class ChatMessageController {

    @Autowired
    private ChatMessageService chatMessageService;

    /**
     * 获取当前登录用户ID与表名
     */
    private static class SessionUser {
        Long userId; String tableName;
        SessionUser(Long userId, String tableName){this.userId=userId;this.tableName=tableName;}
    }

    private SessionUser current(HttpServletRequest request){
        Long userId = (Long) request.getSession().getAttribute("userId");
        String tableName = (String) request.getSession().getAttribute("tableName");
        return new SessionUser(userId, tableName);
    }

    /**
     * 发送消息（文本）
     */
    @PostMapping("/send")
    public R send(@RequestBody ChatMessage body, HttpServletRequest request){
        SessionUser su = current(request);
        if(su.userId == null || su.tableName == null){
            return R.error("未登录或会话已失效");
        }
        if(body.getReceiverId() == null || body.getReceiverTable() == null){
            return R.error("缺少接收方标识");
        }
        if(body.getContent()==null || body.getContent().trim().isEmpty()){
            return R.error("消息内容不能为空");
        }
        ChatMessage entity = new ChatMessage();
        entity.setSenderId(su.userId);
        entity.setSenderTable(su.tableName);
        entity.setReceiverId(body.getReceiverId());
        entity.setReceiverTable(body.getReceiverTable());
        entity.setContent(body.getContent());
        entity.setMsgType(body.getMsgType()==null?"text":body.getMsgType());
        entity.setIsRead(0);
        entity.setCreateTime(new Date());
        chatMessageService.insert(entity);
        return R.ok().put("data", entity);
    }

    /**
     * 会话消息分页（双向）
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String,Object> params, HttpServletRequest request){
        SessionUser su = current(request);
        if(su.userId == null){
            return R.error("未登录");
        }
        // 从params中获取peerId和peerTable
        Long peerId = params.get("peerId") != null ? Long.valueOf(params.get("peerId").toString()) : null;
        String peerTable = params.get("peerTable") != null ? params.get("peerTable").toString() : null;
        if(peerId == null || peerTable == null){
            return R.error("缺少会话对象");
        }
        EntityWrapper<ChatMessage> ew = new EntityWrapper<>();
        ew.andNew().eq("sender_id", su.userId).eq("sender_table", su.tableName).eq("receiver_id", peerId).eq("receiver_table", peerTable)
          .or().eq("sender_id", peerId).eq("sender_table", peerTable).eq("receiver_id", su.userId).eq("receiver_table", su.tableName);
        // 排序
        MPUtil.sort(ew, params);
        PageUtils page = chatMessageService.queryPage(params, ew);
        // 将对方向自己的未读置为已读
        chatMessageService.markAsRead(su.userId, su.tableName, peerId, peerTable);
        return R.ok().put("data", page);
    }

    /**
     * 未读数
     */
    @RequestMapping("/unreadCount")
    public R unreadCount(@RequestParam Map<String,Object> params, HttpServletRequest request){
        SessionUser su = current(request);
        if(su.userId == null){
            return R.error("未登录");
        }
        // 从params中获取peerId和peerTable
        Long peerId = params.get("peerId") != null ? Long.valueOf(params.get("peerId").toString()) : null;
        String peerTable = params.get("peerTable") != null ? params.get("peerTable").toString() : null;
        EntityWrapper<ChatMessage> ew = new EntityWrapper<>();
        ew.eq("receiver_id", su.userId).eq("receiver_table", su.tableName).eq("is_read", 0);
        if(peerId!=null && peerTable!=null){
            ew.eq("sender_id", peerId).eq("sender_table", peerTable);
        }
        int count = chatMessageService.selectCount(ew);
        return R.ok().put("count", count);
    }

    /**
     * 标记已读
     */
    @PostMapping("/read")
    public R markRead(@RequestBody Map<String,Object> params, HttpServletRequest request){
        SessionUser su = current(request);
        if(su.userId == null){
            return R.error("未登录");
        }
        // 从请求体中获取peerId和peerTable
        Long peerId = params.get("peerId") != null ? Long.valueOf(params.get("peerId").toString()) : null;
        String peerTable = params.get("peerTable") != null ? params.get("peerTable").toString() : null;
        if(peerId == null || peerTable == null){
            return R.error("缺少会话对象");
        }
        int updated = chatMessageService.markAsRead(su.userId, su.tableName, peerId, peerTable);
        return R.ok().put("updated", updated);
    }
}