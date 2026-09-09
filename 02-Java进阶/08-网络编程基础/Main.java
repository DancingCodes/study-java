import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// 练习 3：HTTP 请求
// 用 HttpClient 发送 GET 请求到 https://httpbin.org/get
// 打印状态码和响应体
public class Main {
    public static void main(String[] args) throws Exception {
        // TODO: 在这里写代码
        // 创建 HttpClient
        HttpClient client = HttpClient.newHttpClient();

        // 构建 GET 请求
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        // 发送请求，获取响应
        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());

        // 打印状态码和响应体
        System.out.println("状态码：" + response.statusCode());
        System.out.println("响应体：" + response.body());
    }
}
