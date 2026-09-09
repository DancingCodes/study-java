// 题目 1：try/catch 练习
// - 写一个 divide(int a, int b) 方法
//   - 如果 b 为 0，用 throw 抛出 ArithmeticException，消息为 "除数不能为0"
//   - 否则返回 a / b
// - 在 main 中调用，用 try/catch 捕获并打印错误信息
// - 加上 finally 打印 "计算结束"
//
// 题目 2：自定义异常
// - 写一个 setAge(int age) 方法，如果 age < 0 或 age > 150，抛出 AgeException
// - 在 main 中测试合法和非法的 age，用 try/catch 捕获

public class Main {
    public static int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("除数不能为0");
        }
        return a / b;
    }

    public static void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new AgeException("年龄不合法");
        }
    }

    public static void main(String[] args) {
        try {
            divide(1,0);
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("计算结束");
        }


        try {
            setAge(100);
        } catch (AgeException e) {
            System.out.println(e.getMessage());
        }

        try {
            setAge(1000);
        } catch (AgeException e) {
            System.out.println(e.getMessage());
        }
    }
}
