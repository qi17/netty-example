package org.itstack.demo.netty.client;

import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;
import org.itstack.demo.netty.codec.HeartbeatMessage;

import java.util.concurrent.TimeUnit;

/**
 * 客户端心跳发送器
 * @author qich
 * @description 定期发送心跳包到服务端
 * @date 2025-12-06
 */
public class HeartbeatSender {

    private final Channel channel;
    private final Timer timer;
    private final long heartbeatInterval; // 心跳间隔（秒）

    // 使用HashedWheelTimer作为定时器，这是Netty推荐的高效定时器
    public HeartbeatSender(Channel channel, long heartbeatInterval) {
        this.channel = channel;
        this.heartbeatInterval = heartbeatInterval;
        this.timer = new HashedWheelTimer();
    }

    /**
     * 启动心跳发送
     */
    public void start() {
        if (channel == null || !channel.isActive()) {
            System.err.println("Channel is not active, cannot start heartbeat");
            return;
        }

        // timer 定时心跳
        TimerTask heartbeatTask = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                if (channel.isActive()) {
                    // 创建心跳消息
                    HeartbeatMessage pingMessage = new HeartbeatMessage(HeartbeatMessage.HeartbeatType.PING);

                    // 将心跳消息转换为JSON字符串
                    String heartbeatJson = com.alibaba.fastjson.JSON.toJSONString(pingMessage);

                    // 发送心跳
                    channel.writeAndFlush(heartbeatJson + "\r\n");
                    System.out.println("Send heartbeat: " + pingMessage);

                    // 调度下一次心跳
                    timer.newTimeout(this, heartbeatInterval, TimeUnit.SECONDS);
                } else {
                    System.err.println("Channel is inactive, stop heartbeat");
                }
            }
        };

        // 延迟1秒后开始第一次心跳，然后每隔heartbeatInterval秒发送一次
        timer.newTimeout(heartbeatTask, 1, TimeUnit.SECONDS);
    }

    /**
     * 停止心跳发送
     */
    public void stop() {
        if (timer != null) {
            timer.stop();
        }
    }
}