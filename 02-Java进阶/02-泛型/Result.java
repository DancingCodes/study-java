// 题目 1：泛型类
// 写一个 Result<T> 类，模拟接口返回结果：
// - 属性：boolean success、String message、T data
// - 有参构造器
// - getter 方法

public class Result<T> {
    boolean success;
    String message;
    T data;
    public Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean getSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
