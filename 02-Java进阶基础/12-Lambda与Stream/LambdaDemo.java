import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class LambdaDemo {
    public static void main(String[] args) {
        // ===== Lambda =====

        ArrayList<String> names = new ArrayList<>();
        names.add("张三");
        names.add("李四");
        names.add("王五");

        // Lambda 遍历
        names.forEach(name -> System.out.println(name));

        // Lambda 排序
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(3);
        nums.add(1);
        nums.add(5);
        nums.add(2);
        nums.add(4);

        Collections.sort(nums, (a, b) -> a - b);
        System.out.println(nums);  // [1, 2, 3, 4, 5]

        // ===== Stream =====

        // filter 筛选大于 3 的
        List<Integer> filtered = nums.stream()
            .filter(n -> n > 3)
            .collect(Collectors.toList());
        System.out.println(filtered);  // [4, 5]

        // map 每个数乘以 2
        List<Integer> doubled = nums.stream()
            .map(n -> n * 2)
            .collect(Collectors.toList());
        System.out.println(doubled);  // [2, 4, 6, 8, 10]

        // filter + map 链式组合
        List<Integer> result = nums.stream()
            .filter(n -> n > 2)
            .map(n -> n * 10)
            .collect(Collectors.toList());
        System.out.println(result);  // [30, 40, 50]

        // count 计数
        long count = nums.stream().filter(n -> n > 3).count();
        System.out.println(count);  // 2

        // sum 求和
        int sum = nums.stream().mapToInt(n -> n).sum();
        System.out.println(sum);  // 15
    }
}
