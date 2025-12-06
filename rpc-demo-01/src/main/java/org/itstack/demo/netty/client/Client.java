package org.itstack.demo.netty.client;


import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelFuture;
import org.itstack.demo.netty.future.SyncWrite;
import org.itstack.demo.netty.msg.Request;
import org.itstack.demo.netty.msg.Response;

/**
 * @author qich
 * @description
 * @date 2025-12-04 9:23 PM
 */
public class Client {
    private static ChannelFuture future;
    public static void main(String[] args) {
        ClientSocket client = new ClientSocket();
        new Thread(client).start();

        while (true){
            try {
                if ( null == future){
                    future = client.getFuture();
                    Thread.sleep(1000);
                    continue;
                }
                Request request = new Request();
                request.setResult(">>>>> 客户端查询用户信息");

                SyncWrite syncWrite = new SyncWrite();
                Response response = syncWrite.writeAndSync(future.channel(), request, 1000);
                System.out.println("<<<<< 调用结果：" + JSON.toJSON(response));
                Thread.sleep(1000);


            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }
}
