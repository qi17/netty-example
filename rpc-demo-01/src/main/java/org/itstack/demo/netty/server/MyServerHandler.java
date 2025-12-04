package org.itstack.demo.netty.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.itstack.demo.netty.msg.Request;
import org.itstack.demo.netty.msg.Response;

/**
 * @author qich
 * @description
 * @date 2025-12-04 5:25 PM
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            // 服务端收到的消息叫 Request
            Request req = (Request) msg;
            // 收到Request后,构建响应Response
            Response response = buildResp(req);
            // 返回给客户端
            ctx.writeAndFlush(response);
        } finally {
            // 安全释放，只有实现了 ReferenceCounted 的对象才会被释放
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private static Response buildResp(Request req) {
        Response response = new Response();
        response.setRequestId(req.getRequestId());
        response.setParam(req.getResult() + " 请求成功");
        return response;
    }
}
