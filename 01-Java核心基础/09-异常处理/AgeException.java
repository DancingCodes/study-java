// 练习 2：自定义异常
// 定义一个 AgeException，继承 RuntimeException
// 构造器接收 String message，调用 super(message)

public class AgeException extends RuntimeException {
    public AgeException(String message) {
        super(message);
    }
}
