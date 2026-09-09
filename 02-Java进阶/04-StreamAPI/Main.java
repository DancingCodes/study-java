import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        // 练习 1：基础操作
        // 给定 nums，用 Stream 完成：过滤出大于 5 的数 → 升序排序 → 收集成新 List 并打印
        List<Integer> nums = List.of(5, 3, 8, 1, 9, 2, 7, 4, 6);

        List<Integer> newNums = nums.stream().filter(i -> i > 5).sorted().collect(Collectors.toList());
        System.out.println(newNums);


        // 练习 2：字符串处理
        // 给定 words，用 Stream 完成：去重 → 转大写 → 按字母排序 → 用逗号拼接成一个字符串并打印
        List<String> words = List.of("hello", "world", "java", "stream", "hello", "java");

        String newWords = words.stream().distinct().map(String::toUpperCase).sorted().collect(Collectors.joining(","));
        System.out.println(newWords);


        // 练习 3：统计
        // 给定 scores，用 Stream 完成：
        // - 打印及格（>= 60）的人数
        // - 打印最高分和最低分
        // - 打印所有及格分数的平均值
        List<Integer> scores = List.of(85, 92, 78, 95, 60, 45, 88, 72);

        long count = scores.stream().filter(s -> s >= 60).count();
        System.out.println(count);

        Optional<Integer> max = scores.stream().max(Integer::compareTo);
        Optional<Integer> min = scores.stream().min(Integer::compareTo);
        System.out.println(max);
        System.out.println(min);

        double avg = scores.stream().filter(s -> s >= 60).mapToInt(Integer::intValue).average().getAsDouble();
        System.out.println(avg);
    }
}
