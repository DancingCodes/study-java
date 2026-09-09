// 题目 1：匿名内部类
// - 用匿名内部类分别创建「加法」和「减法」两个 Calculator 实现
// - 写一个 printResult(Calculator calc, int a, int b) 方法，打印计算结果
//
// 题目 2：枚举
// - 遍历所有 Weekday，打印中文名
// - 用 switch 判断某个 Weekday 是工作日还是休息日

public class Main {
    public static void printResult(Calculator calc, int a, int b) {
        System.out.println(calc.compute(a, b));
    }


    public static void main(String[] args) {
        Calculator add = new Calculator() {
            @Override
            public int compute(int a, int b) {
                return a + b;
            }
        };

        Calculator sub = new Calculator() {
            @Override
            public int compute(int a, int b) {
                return a - b;
            }
        };

        printResult(add, 2, 1);
        printResult(sub, 2, 1);



        for (Weekday day : Weekday.values()) {
            System.out.println(day.getName());
            switch (day) {
                case Saturday -> System.out.println("休息日");
                case Sunday -> System.out.println("休息日");
                default -> System.out.println("工作日");
            }
        }
    }
}
