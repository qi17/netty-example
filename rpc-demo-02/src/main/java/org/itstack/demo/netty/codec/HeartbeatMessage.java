package org.itstack.demo.netty.codec;

import java.io.Serializable;

/**
 * 心跳消息实体类
 * @author qich
 * @description 用于客户端主动发送心跳
 * @date 2025-12-06
 */
public class HeartbeatMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 心跳类型：PING(客户端发送) / PONG(服务端响应)
     */
    private HeartbeatType type;

    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 消息内容
     */
    private String message;

    public enum HeartbeatType {
        PING,   // 客户端发送的心跳请求
        PONG    // 服务端响应的心跳
    }

    public HeartbeatMessage() {
        this.timestamp = System.currentTimeMillis();
    }

    public HeartbeatMessage(HeartbeatType type) {
        this();
        this.type = type;
        if (type == HeartbeatType.PING) {
            this.message = "HEARTBEAT_PING";
        } else {
            this.message = "HEARTBEAT_PONG";
        }
    }

    public HeartbeatMessage(HeartbeatType type, String message) {
        this(type);
        this.message = message;
    }

    public HeartbeatType getType() {
        return type;
    }

    public void setType(HeartbeatType type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "HeartbeatMessage{" +
                "type=" + type +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}