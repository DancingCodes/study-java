public class Main {
    // 练习 1：写一个 max 方法
    // 接收两个 int 参数，返回较大的那个
    // TODO: 在这里写代码
    public static int max(int a, int b) {
        return a > b ? a : b;
    }

    // 练习 2：方法重载
    // 写三个 multiply 方法：
    // multiply(int, int) → 两个整数的乘积
    // multiply(double, double) → 两个小数的乘积
    // multiply(int, int, int) → 三个整数的乘积
    // TODO: 在这里写代码
    public static int multiply(int a, int b) {
        return a * b;
    }

    public static double multiply(double a, double b) {
        return a * b;
    }

    public static int multiply(int a, int b, int c) {
        return a * b * c;
    }

    // 练习 3：可变参数求平均值
    // 写一个 average 方法，接收 double... numbers，返回平均值
    // TODO: 在这里写代码
    public static double average(double... numbers) {
        double sum = 0;
        for (double number : numbers) {
            sum += number;
        }
        return sum / numbers.length;
    }

    // 练习 4：递归求阶乘
    // 写一个 factorial 方法，用递归计算 n 的阶乘
    // TODO: 在这里写代码
    public static int factorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    // 练习 5：递归求和
    // 写一个 sumTo 方法，用递归计算 1 + 2 + ... + n
    // TODO: 在这里写代码
    public static int sumTo(int n) {
        if (n == 1) {
            return 1;
        }
        return n + sumTo(n - 1);
    }

    public static void main(String[] args) {
        System.out.println(max(1, 2));
        System.out.println(multiply(1, 2));
        System.out.println(multiply(1.0, 2.0));
        System.out.println(multiply(1, 2, 3));
        System.out.println(average(1, 2, 3));
        System.out.println(factorial(3));
        System.out.println(sumTo(3));
    }
}
