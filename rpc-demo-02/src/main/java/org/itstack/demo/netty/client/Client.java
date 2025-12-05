package org.itstack.demo.netty.client;


import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author qich
 * @description
 * @date 2025-12-04 9:23 PM
 */
public class Client {

    public static void main(String[] args) {
        Client client = new Client();
        client.connect("127.0.0.1", 7397);
    }

    public void connect(String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ClientChannelInitializer());
            ChannelFuture f = b.connect(host, port).sync();
            //添加监听 失败重连
            f.addListener(new ClientChannelFutureListener());
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}
