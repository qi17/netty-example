package org.itstack.demo.netty.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.itstack.demo.netty.util.SerializationUtil;

import java.util.List;

/**
 * @author qich
 * @description
 * @date 2025-12-04 5:41 PM
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> out) throws Exception {
        //  和通信协议有关
        //  等待消息足够长 最短的消息>4 字节
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        // 处理tcp粘包和半包
        byteBuf.markReaderIndex();
        int length = byteBuf.readInt();
        if (byteBuf.readableBytes() < length) {
            // 消息还没完全接受
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        out.add(SerializationUtil.deserialize(bytes, genericClass));
    }
}
