package com.example.text.server;


import io.netty.channel.*;

import java.net.InetAddress;
import java.util.Date;

/**
 * @author qich
 * @description
 * @date 2025-11-24 5:03 PM
 */
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 客户端注册打印
        System.out.println("Client connected: " + ctx.channel());

        // 返回消息给注册的客户端
        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.write("Now is " + new Date() + "\r\n");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        String resp;

        boolean close = false;
        if (msg.isEmpty()) {
            resp = "Please type something.\r\n";
        } else if ("bye".equals(msg.toLowerCase())) {
            resp = "Have a good day!\r\n";
            close = true;
        } else {
            resp = "Did you say '" + msg + "'?\r\n";
        }

        ChannelFuture future = ctx.writeAndFlush(resp);

        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
