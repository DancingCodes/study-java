import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Main {
    public static void main(String[] args) {
        // 题目 1：String 练习
        // 给定字符串 "  Hello, World! Java is Great!  "
        // - 去除首尾空格
        // - 转成全小写
        // - 把 "great" 替换成 "awesome"
        // - 用 "," 分割，打印每一段（每段也 trim 一下）
        String message = "  Hello, World! Java is Great!  ";
        String[] parts = message.trim().toLowerCase().replace("great", "awesome").split(",");
        for (String s : parts) {
            System.out.println(s.trim());
        }

        // 题目 2：StringBuilder 练习
        // 用 StringBuilder 拼接 1 到 100，用逗号分隔："1,2,3,...,100"
        // 注意最后一个数字后面不要有逗号
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 100; i++) {
            sb.append(i);
            if (i != 100) {
                sb.append(",");
            }
        }
        System.out.println(sb.toString());

        // 题目 3：日期练习
        // - 获取今天的日期，打印年、月、日
        // - 计算 100 天后是几月几号
        // - 把日期格式化成 "yyyy年MM月dd日" 的格式打印
        LocalDate today = LocalDate.now();
        System.out.println(today.getYear());
        System.out.println(today.getMonthValue());
        System.out.println(today.getDayOfMonth());

        LocalDate future = today.plusDays(100);
        System.out.println(future);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        System.out.println(today.format(fmt));
    }
}
