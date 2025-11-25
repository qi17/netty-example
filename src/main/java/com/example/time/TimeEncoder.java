package com.example.time;


import com.example.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.bouncycastle.util.encoders.BufferedEncoder;

/**
 * @author qich
 * @description 编码器
 * @date 2025-11-24 2:52 PM
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int)m.value());
        // promise 可以标识成功或者失败
        ctx.write(encoded,promise);

    }
}
