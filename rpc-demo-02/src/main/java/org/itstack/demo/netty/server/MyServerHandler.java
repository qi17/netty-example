package org.itstack.demo.netty.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qich
 * @description
 * @date 2025-12-04 5:25 PM
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.userEventTriggered(ctx, msg);
        // 心跳检测
        if (msg instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) msg;
            if (e.state() == IdleState.READER_IDLE) {
                System.out.println(" 提醒=> Reader Idle");
                ctx.writeAndFlush("读取等待：客户端你在吗[ctx.close()]{我结尾是一个换行符用于处理半包粘包}... ...\r\n");
                // 客户端长时间没有发送数据 主动关闭连接
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                System.out.println(" 提醒=> Write Idle");
                ctx.writeAndFlush("写入等待：客户端你在吗{我结尾是一个换行符用于处理半包粘包}... ...\r\n");
            } else if (e.state() == IdleState.ALL_IDLE) {
                System.out.println(" 提醒=> All_IDLE");
                ctx.writeAndFlush("全部时间：客户端你在吗{我结尾是一个换行符用于处理半包粘包}... ...\r\n");
            }
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告IP:" + channel.localAddress().getHostString() + ",Port:" + channel.localAddress().getPort());
        //通知客户端链接建立成功
        String str = "通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(str);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开链接" + ctx.channel().localAddress().toString());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " 接收到消息：" + msg);
//        //通知客户端链消息发送成功
//        String str = "服务端收到：" + new Date() + " " + msg + "\r\n";
//        ctx.writeAndFlush(str);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }
}
