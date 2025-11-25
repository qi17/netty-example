package com.example.time;


import com.example.discord.DiscordServer;
import com.example.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author qich
 * @description
 * @date 2025-11-23 9:21 PM
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    //不接受任何消息 --> channelActive（ 一旦建立就触发）
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        /// 关于套接字缓冲区 比如发了三条有序的消息 ABC DEF HIG 但是缓冲区不是队列它会按照字节流处理
        /// 服务端接受按照流接受很可能出现碎片化 BCEFGIHA
        /// 因此无论发送还是接收都需要按帧处理

        final ChannelFuture future = ctx.writeAndFlush(new UnixTime());

        future.addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
