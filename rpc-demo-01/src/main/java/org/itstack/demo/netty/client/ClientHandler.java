package org.itstack.demo.netty.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.itstack.demo.netty.future.SyncWriteFuture;
import org.itstack.demo.netty.future.SyncWriteMap;
import org.itstack.demo.netty.msg.Response;

/**
 * @author qich
 * @description
 * @date 2025-12-04 9:06 PM
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //客户端收到服务段的响应即Response
        Response resp = (Response) msg;
        String requestId = resp.getRequestId();
        SyncWriteFuture syncWriteFuture = (SyncWriteFuture) SyncWriteMap.syncKey.get(requestId);
        if (null != syncWriteFuture) {
            syncWriteFuture.setResponse(resp);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
