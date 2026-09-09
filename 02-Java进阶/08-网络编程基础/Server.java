import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

// 练习 1：Socket 服务器
// 监听 9999 端口，收到客户端消息后：
// - 打印收到的消息
// - 把消息转成大写回复给客户端
// - 例：客户端发 "hello java"，服务器回复 "HELLO JAVA"
public class Server {
    public static void main(String[] args) throws IOException {
        // TODO: 在这里写代码
        // 1. 创建 ServerSocket，监听 9999 端口
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务器启动，等待连接...");

        // 2. 等待客户端连接（会阻塞，直到有客户端连接）
        Socket socket = serverSocket.accept();
        System.out.println("客户端已连接：" + socket.getInetAddress());

        // 3. 获取输入流，读客户端发来的数据
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        String message = reader.readLine();
        System.out.println("收到消息：" + message);

        // 4. 获取输出流，给客户端回消息
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true);
        writer.println("服务器收到：" + message.toUpperCase());

        // 5. 关闭
        socket.close();
        serverSocket.close();
    }
}
