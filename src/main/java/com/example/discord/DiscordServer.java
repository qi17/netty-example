package com.example.discord;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author qich
 * @description
 * @date 2025-11-23 12:56
 */
public class DiscordServer {
    private int port;

    public DiscordServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        //多线程事件循环，用于处理 I/O 操作
        EventLoopGroup group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 实例化新的Channel来接受传入的连接
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture sync = b.bind(port).sync();

            sync.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        try {
            new DiscordServer(8080).run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
