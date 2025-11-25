package com.example.time;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author qich
 * @description
 * @date 2025-11-24 9:53 AM
 */
public class TimeClient {
    public static void main(String[] args) throws Exception{
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        MultiThreadIoEventLoopGroup worker = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        try{
            // Bootstrap 适用于非服务器通道
            Bootstrap b = new Bootstrap();
            b.group(worker)
                    // 客户端使用 NioSocketChannel
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeDecoder(),new TimeClientHandler());
                        }
                    });
            ChannelFuture f = b.connect(host, port).sync();

            f.channel().closeFuture().sync();
        }finally {
            worker.shutdownGracefully();
        }
    }
}
