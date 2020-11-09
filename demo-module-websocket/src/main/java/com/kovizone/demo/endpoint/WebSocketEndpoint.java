package com.kovizone.demo.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
@ServerEndpoint("/websocket/{sid}")
@Component
@Slf4j
public class WebSocketEndpoint {

    public WebSocketEndpoint() {
        super();
    }

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<WebSocketEndpoint> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接收sid
     */
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("开始监听：" + sid + ",当前在线人数为" + getOnlineCount());
        this.sid = sid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info(sid + "连接关闭，当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自" + sid + "的信息：" + message);
        //群发消息
        for (WebSocketEndpoint item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发生异常回调方法
     *
     * @param session 会话
     * @param e       异常
     */
    @OnError
    public void onError(Session session, Throwable e) {
        log.error(e.getMessage(), e);
    }

    /**
     * 实现服务器主动推送
     *
     * @param message 消息
     * @throws IOException IO异常
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     *
     * @param message 消息
     * @param sid     指定sid
     * @throws IOException IO异常
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到" + sid + "，推送内容：" + message);
        for (WebSocketEndpoint item : webSocketSet) {
            //这里可以设定只推送给这个sid的，为null则全部推送
            if (sid == null) {
                item.sendMessage(message);
            } else if (item.sid.equals(sid)) {
                item.sendMessage(message);
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketEndpoint.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketEndpoint.onlineCount--;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.session, this.sid);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof WebSocketEndpoint)) {
            return false;
        }
        return obj.hashCode() == this.hashCode();
    }
}