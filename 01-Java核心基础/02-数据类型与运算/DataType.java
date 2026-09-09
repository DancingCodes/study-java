import java.math.BigDecimal;

public class DataType {
    public static void main(String[] args) {
        // 题目 1：类型转换
        // - 一个 int 变量赋值给 double（自动转换）
        int intValue = 1;
        double autoToDouble = intValue;
        System.out.println(autoToDouble);

        // - 一个 double 变量强制转成 int（观察小数部分去哪了）
        double decimalValue = 9.99;
        int truncated = (int) decimalValue;
        System.out.println(truncated);

        // - 一个值为 200 的 int 强制转成 byte（观察溢出结果）
        int bigInt = 200;
        byte overflow = (byte) bigInt;
        System.out.println(overflow);

        // 题目 2：精度问题
        // - 打印 0.1 + 0.2 的结果
        System.out.println(0.1 + 0.2);

        // - 用 BigDecimal 计算 0.1 + 0.2，打印精确结果
        BigDecimal num1 = new BigDecimal("0.1");
        BigDecimal num2 = new BigDecimal("0.2");
        System.out.println(num1.add(num2));

        // 题目 3：Integer 缓存
        // - 值为 100 的一对（缓存范围内）
        Integer small1 = 100;
        Integer small2 = 100;
        System.out.println(small1 == small2);
        System.out.println(small1.equals(small2));

        // - 值为 200 的一对（超出缓存）
        Integer big1 = 200;
        Integer big2 = 200;
        System.out.println(big1 == big2);
        System.out.println(big1.equals(big2));

        // 题目 4：整数除法
        System.out.println(10 / 3);
        System.out.println((double) 10 / 3);
        System.out.println(10.0 / 3);
    }
}
