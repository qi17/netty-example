package org.itstack.demo.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.itstack.demo.netty.util.MsgUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class NettyClient {

    public static void main(String[] args) {
        new NettyClient().connect("127.0.0.1", 7397);
    }

    private void connect(String inetHost, int inetPort) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new MyChannelInitializer());
            Channel channel = b.connect(inetHost, inetPort).sync().channel();
            System.out.println("itstack-demo-netty client start done.");

            ChannelFuture lastFuture = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;){
                String line = in.readLine();
                if (line == null) break;
                // 发送到服务端 一定要注意协议！！！
                lastFuture = channel.writeAndFlush(MsgUtil.buildMsg(channel.id().toString(), line));

                if (line.equals("bye")) {
                    channel.closeFuture().sync();
                    break;
                }
            }
            // 等待消息发送完成
            if (lastFuture!=null){
                lastFuture.sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
