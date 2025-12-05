package org.itstack.demo.netty.server;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;

/**
 * @author qich
 * @description
 * @date 2025-12-05 9:43 AM
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        /**
         * 心跳监测
         * 1、readerIdleTimeSeconds 读超时时间
         * 2、writerIdleTimeSeconds 写超时时间
         * 3、allIdleTimeSeconds    读写超时时间
         * 4、TimeUnit.SECONDS 秒[默认为秒，可以指定]
         */
        channel.pipeline().addLast(new IdleStateHandler(2, 2, 2));
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
        channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
        channel.pipeline().addLast(new MyServerHandler());

    }
}
