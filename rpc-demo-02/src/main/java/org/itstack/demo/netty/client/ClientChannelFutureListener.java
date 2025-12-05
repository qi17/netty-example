package org.itstack.demo.netty.client;


import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * @author qich
 * @description
 * @date 2025-12-05 10:06 AM
 */
public class ClientChannelFutureListener  implements ChannelFutureListener {
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
            System.out.println("rpc - heart - beat - client start done. ");
            return;
        }
        final EventLoop loop = channelFuture.channel().eventLoop();
        loop.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    new Client().connect("127.0.0.1", 7397);
                    System.out.println("rpc - heart - beat - client start done.");
                    Thread.sleep(500);
                } catch (Exception e){
                    System.out.println("rpc - heart - beat - client start error go reconnect ...");
                }
            }
        }, 1L, TimeUnit.SECONDS);
    }
}
