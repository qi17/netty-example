package com.example.wechat.server;


import com.alibaba.fastjson.JSONObject;
import com.example.wechat.ChannelHolder;
import com.example.wechat.MsgUtil;
import com.example.wechat.protocol.ClientMsgProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;


/**
 * @author qich
 * @description
 * @date 2025-12-04 3:47 PM
 */
public class CustomServerHandler extends ChannelInboundHandlerAdapter {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CustomServerHandler.class);
    private WebSocketServerHandshaker handshaker;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel socketChannel = (SocketChannel) ctx.channel();
        logger.info("客户端链接到本服务端,报告IP:{},PORT:{}", socketChannel.localAddress().getHostString(),socketChannel.localAddress().getPort());
        ChannelHolder.channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端断开连接：{}" , ctx.channel().localAddress().toString());
        ChannelHolder.channelGroup.remove(ctx.channel());
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 区分消息是http 还是 ws

        //http
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest httpRequest = (FullHttpRequest) msg;

            if (!httpRequest.decoderResult().isSuccess()) {
                DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);

                // 返回应答给客户端
                if (httpResponse.status().code() != 200) {
                    ByteBuf buf = Unpooled.copiedBuffer(httpResponse.status().toString(), CharsetUtil.UTF_8);
                    httpResponse.content().writeBytes(buf);
                    buf.release();
                }

                // 非Keep-Alive，关闭连接
                ChannelFuture f = ctx.channel().writeAndFlush(httpResponse);
                if (httpResponse.status().code() != 200) {
                    f.addListener(ChannelFutureListener.CLOSE);
                }
                return;
            }
            // 构建ws
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws:/" + ctx.channel() + "/websocket", null, false);
            handshaker = wsFactory.newHandshaker(httpRequest);
            if (null == handshaker) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), httpRequest);
            }
        }
        //ws

        if (msg instanceof WebSocketFrame){
            WebSocketFrame frame = (WebSocketFrame) msg;


            if (frame instanceof CloseWebSocketFrame){
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
                return;
            }

            if (frame instanceof PingWebSocketFrame){
                ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
                return;
            }
            if (! (frame instanceof TextWebSocketFrame)){
                throw new Exception("仅支持文本传输");
            }

            String textMsg = ((TextWebSocketFrame) frame).text();

            System.out.println("接收到文本消息：" + textMsg);

            ClientMsgProtocol clientMsgProtocol = JSONObject.parseObject(textMsg,ClientMsgProtocol.class);

            switch (clientMsgProtocol.getType()){
                //1请求个人信息
                case 1:
                    ctx.channel().writeAndFlush(MsgUtil.buildMsgOwner(ctx.channel().id().toString()));
                    return;
                //群发消息
                case 2:
                    TextWebSocketFrame textWsFrame = MsgUtil.buildMsgAll(ctx.channel().id().toString(), clientMsgProtocol.getMsgInfo());
                    ChannelHolder.channelGroup.writeAndFlush(textWsFrame);
                    return;
                default:
                    break;
            }
        }

        return;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.info("异常信息：\r\n" + cause.getMessage());

    }
}
