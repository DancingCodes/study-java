// 题目 3：定义一个 Calculator 类
// add(int a, int b) 返回和
// subtract(int a, int b) 返回差
// multiply(int a, int b) 返回积
// divide(int a, int b) 返回商（注意处理除以 0）
// 这些方法不需要 static，通过 new Calculator() 创建对象来调用

public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            System.out.println("除数不能为 0");
            return 0;
        }
        return a / b;
    }
}