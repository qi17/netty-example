package org.itstack.demo.netty.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qich
 * @description
 * @date 2025-12-04 9:06 PM
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告信息：本客户端链接到服务端。channelId：" + channel.id());
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开链接重连" + ctx.channel().localAddress().toString());
        //使用过程中断线重连
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new Client().connect("127.0.0.1", 7397);
                    System.out.println("rpc - heart - beat - client start done. ");
                    Thread.sleep(500);
                } catch (Exception e){
                    System.out.println("rpc - heart - beat - client start error go reconnect ... ");
                }
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常信息，断开重连：\r\n" + cause.getMessage());
        ctx.close();
    }
}
