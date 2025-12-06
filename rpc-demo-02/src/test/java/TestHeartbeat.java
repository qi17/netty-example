import org.itstack.demo.netty.client.Client;
import org.itstack.demo.netty.server.StartServer;

/**
 * 测试心跳功能的简单类
 */
public class TestHeartbeat {
    public static void main(String[] args) {
        // 启动服务端
        new Thread(() -> {
            try {
                StartServer.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // 等待服务端启动
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 启动客户端
        new Thread(() -> {
            try {
                Client.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}