package org.itstack.demo.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;


public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) {
        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));

        channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));

        channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));

        channel.pipeline().addLast(new MyServerHandler());
    }

}
