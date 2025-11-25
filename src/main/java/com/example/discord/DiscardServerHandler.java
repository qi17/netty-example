package com.example.discord;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author qich
 * @description
 * @date 2025-11-23 12:51
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // todo echo协议/回声（将收到的任何数据原封不动地发回给发送者）
        // 写到缓冲区
        ctx.write(msg);
        // 刷新到网络 即返回给客户端
        ctx.flush();

//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) {
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            // 释放引用计数对象
//            //  ((ByteBuf) msg).release();
//            ReferenceCountUtil.release(msg);
//        }
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @author qich
     * @date 2025/11/23
     */

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }
}
