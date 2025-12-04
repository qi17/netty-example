package org.itstack.demo.netty.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.itstack.demo.netty.codec.RpcDecoder;
import org.itstack.demo.netty.codec.RpcEncoder;
import org.itstack.demo.netty.msg.Request;
import org.itstack.demo.netty.msg.Response;

/**
 * @author qich
 * @description
 * @date 2025-12-04 9:04 PM
 */
public class ClientSocket implements Runnable{
    private ChannelFuture future;

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group( group);
            bootstrap.channel( NioSocketChannel.class);
            bootstrap.option( ChannelOption.AUTO_READ, true);
            bootstrap.handler( new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new RpcDecoder(Response.class),
                            new RpcEncoder(Request.class),
                            new ClientHandler());
                }
            });
            this.future = bootstrap.connect("127.0.0.1", 7397).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            group.shutdownGracefully();
        }

    }


    public ChannelFuture getFuture() {
        return future;
    }
    public void setFuture(ChannelFuture future) {
        this.future = future;
    }
}
