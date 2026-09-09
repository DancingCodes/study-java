import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        // 练习 1：类型转换
        // - 一个 int 变量赋值给 double（自动转换）
        // - 一个 double 变量强制转成 int（观察小数部分去哪了）
        // - 一个值为 200 的 int 强制转成 byte（观察溢出结果）
        // TODO: 在这里写代码
        int intValue = 1;
        double autoToDouble = intValue;
        System.out.println(autoToDouble);

        double decimalValue = 9.99;
        int truncated = (int) decimalValue;
        System.out.println(truncated);

        int bigInt = 200;
        byte overflow = (byte) bigInt;
        System.out.println(overflow);

        // 练习 2：精度问题
        // - 打印 0.1 + 0.2 的结果
        // - 用 BigDecimal 计算 0.1 + 0.2，打印精确结果
        // TODO: 在这里写代码
        System.out.println(0.1 + 0.2);

        BigDecimal num1 = new BigDecimal("0.1");
        BigDecimal num2 = new BigDecimal("0.2");
        System.out.println(num1.add(num2));

        // 练习 3：Integer 缓存
        // - 值为 100 的一对（缓存范围内），用 == 和 equals 比较
        // - 值为 200 的一对（超出缓存），用 == 和 equals 比较
        // TODO: 在这里写代码
        Integer small1 = 100;
        Integer small2 = 100;
        System.out.println(small1 == small2);
        System.out.println(small1.equals(small2));

        Integer big1 = 200;
        Integer big2 = 200;
        System.out.println(big1 == big2);
        System.out.println(big1.equals(big2));

        // 练习 4：整数除法
        // 分别打印 10/3、(double)10/3、10.0/3 的结果，观察区别
        // TODO: 在这里写代码
        System.out.println(10 / 3);
        System.out.println((double) 10 / 3);
        System.out.println(10.0 / 3);
    }
}
