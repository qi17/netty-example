package com.example.wechat;

import com.example.wechat.server.NettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetSocketAddress;

@SpringBootApplication
public class WechatNettyApplication implements CommandLineRunner {
    @Value("${netty.host}")
    private String host;
    @Value("${netty.port}")
    private int port;
    @Autowired
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(WechatNettyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Start Netty server in a separate thread
        new Thread(() -> {
            InetSocketAddress address = new InetSocketAddress(host, port);
            ChannelFuture channelFuture = nettyServer.start(address);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
            channelFuture.channel().closeFuture().syncUninterruptibly();
        }).start();
    }
}
