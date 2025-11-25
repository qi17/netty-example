package com.example.time;


import com.example.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author qich
 * @description 解码器
 * @date 2025-11-24 2:23 PM
 */
///  ByteToMessageDecoder 解码器 是 ChannelInboundHandler 的一个实现，它可以轻松处理碎片化问题。
public class TimeDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 缓冲区未满暂时不写入
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        list.add(new UnixTime(byteBuf.readUnsignedInt()));
    }
}
