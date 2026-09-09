public class Main {
    public static void main(String[] args) {
        // 练习 1：成绩等级
        // 定义一个分数变量，用 if/else 打印对应等级：
        // 90 及以上：优秀 | 80-89：良好 | 60-79：及格 | 60 以下：不及格
        // TODO: 在这里写代码
        int score = 60;
        if (score >= 90) {
            System.out.println("优秀");
        } else if (score >= 80) {
            System.out.println("良好");
        } else if (score >= 60) {
            System.out.println("及格");
        } else {
            System.out.println("不及格");
        }

        // 练习 2：星期几（switch）
        // 定义 int day = 5，用 switch 箭头语法打印对应的星期几
        // TODO: 在这里写代码
        int day = 5;
        switch (day) {
            case 1:
                System.out.println("周一");
                break;
            case 2:
                System.out.println("周二");
                break;
            case 3:
                System.out.println("周三");
                break;
            case 4:
                System.out.println("周四");
                break;
            case 5:
                System.out.println("周五");
                break;
            case 6:
                System.out.println("周六");
                break;
            case 7:
                System.out.println("周日");
                break;
        }

        // Java 14+ 新写法
        switch (day) {
            case 1 -> System.out.println("周一");
            case 2 -> System.out.println("周二");
            case 3 -> System.out.println("周三");
            case 4 -> System.out.println("周四");
            case 5 -> System.out.println("周五");
            case 6 -> System.out.println("周六");
            case 7 -> System.out.println("周日");
        }

        // 练习 3：求和
        // 用 for 循环计算 1 + 2 + 3 + ... + 100 的结果
        // TODO: 在这里写代码
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println(sum);

        // 练习 4：跳过 3 的倍数
        // 打印 1 到 20，但跳过所有 3 的倍数，用 for + continue 实现
        // TODO: 在这里写代码
        for (int i = 1; i <= 20; i++) {
            if (i % 3 == 0) {
                continue;
            }
            System.out.println(i);
        }

        // 练习 5：九九乘法表
        // 用嵌套 for 循环打印九九乘法表
        // TODO: 在这里写代码
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j + "×" + i + "=" + (i * j) + "\t");
            }
            System.out.println();
        }
    }
}
