package com.sky.handler;

import com.alibaba.fastjson.JSON;
import com.sky.context.BaseContext;
import com.sky.result.MessageResult;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat")
@Component
public class ChatWebSocketEndpoint {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        // 当WebSocket连接建立时
        String currentName = BaseContext.getCurrentName();
        Long currentId = BaseContext.getCurrentId();
        String key = currentId + currentName;
        sessions.put(key, session);
    }

    //message 传入的参数应为json格式，里面包括了要接收消息的用户id、用户名和消息内容
    @OnMessage
    public void onMessage(String message) {
        // 处理收到的消息
        // 解析消息，获取目标用户ID和消息内容
        // 这里简化处理，假设消息格式为 "targetUserId:message"
        MessageResult messageResult = JSON.parseObject(message, MessageResult.class);
        long id = messageResult.getId();
        String name = messageResult.getName();
        String content = messageResult.getMessage();
        String key = id + name;

        // 根据目标用户ID获取对应的WebSocketSession
        Session targetSession = sessions.get(key);

        // 如果目标Session存在且打开，向其发送消息
        if (targetSession != null && targetSession.isOpen()) {
            try {
                targetSession.getBasicRemote().sendText(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        String currentName = BaseContext.getCurrentName();
        Long currentId = BaseContext.getCurrentId();
        String key = currentId + currentName;
        // 当WebSocket连接关闭时
        Session remove = sessions.remove(key);
    }
}
