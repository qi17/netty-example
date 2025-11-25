package com.example.text.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.OffsetDateTime;

/**
 * @author qich
 * @description
 * @date 2025-11-25 9:26 AM
 */
public class TelnetClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = 8023;

    public static void main(String[] args) throws Exception {
        EventLoopGroup g = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        try {
            Bootstrap b = new Bootstrap();
            b.group(g)
                    .channel(NioSocketChannel.class)
                    .handler(new TelnetClientInitializer());

            Channel channel = b.connect(HOST, PORT).sync().channel();
            ChannelFuture lastFuture = null;

            // 通过控制台输入
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;){
                String line = in.readLine();
                if (line == null) break;
                // 发送到服务端
                lastFuture = channel.writeAndFlush(line + "\r\n");

                if (line.equals("bye")) {
                    channel.closeFuture().sync();
                    break;
                }
            }
            // 等待消息发送完成
            if (lastFuture!=null){
                lastFuture.sync();
            }
        }finally {
            g.shutdownGracefully();
        }
    }
}
