import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // 练习 1：ArrayList 练习
        // - 创建一个 List<String> 存 5 个名字
        // - 删除第 2 个元素
        // - 遍历打印所有元素
        // - 判断列表中是否包含 "张三"
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("张四");
        list.add("张五");
        list.add("张六");
        list.add("张七");

        list.remove(1);

        for (String str : list) {
            System.out.println(str);
        }

        System.out.println(list.contains("张三"));


        // 练习 2：HashSet 去重
        // - 创建一个 List<Integer>，添加 1, 2, 3, 2, 4, 3, 5
        // - 用 HashSet 对这个 List 去重
        // - 把去重后的结果转回 List，排序后打印

        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(2);
        list1.add(4);
        list1.add(3);
        list1.add(5);
        Set<Integer> set = new HashSet<>(list1);
        List<Integer> list2 = new ArrayList<>(set);
        list2.sort((o1, o2) -> o1.compareTo(o2));
        System.out.println(list2);


        // 练习 3：HashMap 统计
        // - 给定字符串 "aabbccaabbc"
        // - 用 HashMap 统计每个字符出现的次数
        // - 打印结果，如：a=4, b=4, c=3

        String s = "aabbccaabbc";
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        System.out.println(map);
    }
}
