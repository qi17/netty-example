package com.example.time;


import com.example.discord.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author qich
 * @description
 * @date 2025-11-24 10:03 AM
 */
public class TimeServer {
    private int port;

    public TimeServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        //多线程事件循环，用于处理 I/O 操作
        EventLoopGroup group = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        try {
            //ServerBootstrap 适用于服务器通道
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 服务端使用  NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeEncoder(),new TimeServerHandler());
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
            // rdate 默认使用Time协议 如果我们手动实现客户端则需要手动转int的时间
            // 因macos系统原因 rdate无法使用-o 修改端口 所以直接使用默认的37端口 测试命令: rdate -p localhost
            // new TimeServer(37).run();
            new TimeServer(8080).run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
