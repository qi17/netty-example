package org.itstack.demo.netty.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.itstack.demo.netty.codec.HeartbeatMessage;
import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qich
 * @description
 * @date 2025-12-04 9:06 PM
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private HeartbeatSender heartbeatSender;
    private Boolean heartCheck;

    public ClientHandler(Boolean heartCheck) {
        this.heartCheck = heartCheck;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告信息：本客户端链接到服务端。channelId：" + channel.id());
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        if (heartCheck) {
            // 连接建立后启动心跳发送器，每3秒发送一次心跳
            heartbeatSender = new HeartbeatSender(ctx.channel(), 3);
            heartbeatSender.start();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 停止心跳发送器
        if (heartbeatSender != null) {
            heartbeatSender.stop();
        }

        System.out.println("断开链接重连" + ctx.channel().localAddress().toString());
        //使用过程中断线重连
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Client().connect("127.0.0.1", 7397);
                    System.out.println("rpc - heart - beat - client start done. ");
                    Thread.sleep(500);
                } catch (Exception e) {
                    System.out.println("rpc - heart - beat - client start error go reconnect ... ");
                }
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" + message);

        // 检查是否是心跳响应
        try {
            HeartbeatMessage heartbeatMsg = JSON.parseObject(message, HeartbeatMessage.class);
            if (heartbeatMsg != null && heartbeatMsg.getType() != null) {
                if (heartbeatMsg.getType() == HeartbeatMessage.HeartbeatType.PONG) {
                    System.out.println("接收到心跳响应：PONG");
                }
            }
        } catch (Exception e) {
            // 不是心跳消息，继续处理普通消息
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 停止心跳发送器
        if (heartbeatSender != null) {
            heartbeatSender.stop();
        }
        System.out.println("异常信息，断开重连：\r\n" + cause.getMessage());
        ctx.close();
    }
}
