import java.io.*;
import java.net.Socket;

// 练习 2：Socket 客户端
// 连接 localhost:9999
// 发送 "hello java"
// 打印服务器的回复
public class Client {
    public static void main(String[] args) throws IOException {
        // 1. 创建 Socket，连接服务器
        Socket socket = new Socket("localhost", 9999);
        System.out.println("已连接服务器");

        // 2. 发送消息
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true);
        writer.println("hello java！");

        // 3. 接收服务器回复
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        String response = reader.readLine();
        System.out.println("服务器回复：" + response);

        // 4. 关闭
        socket.close();
    }
}
