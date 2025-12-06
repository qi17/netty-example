package org.itstack.demo.netty.test;

import org.itstack.demo.netty.server.StartServer;

/**
 * 心跳功能测试类
 * @author qich
 * @description 测试客户端主动发送心跳功能
 * @date 2025-12-06
 */
public class HeartbeatTest {

    public static void main(String[] args) throws InterruptedException {
        // 启动服务端
        new Thread(() -> {
            try {
                StartServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 等待服务端启动
        Thread.sleep(2000);

        // 启动客户端
        new Thread(() -> {
            try {
                org.itstack.demo.netty.client.Client.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 测试运行30秒，观察心跳输出
        Thread.sleep(30000);

        System.out.println("Heartbeat test completed.");
    }
}