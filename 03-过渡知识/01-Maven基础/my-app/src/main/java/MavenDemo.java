import com.google.gson.Gson;

public class MavenDemo {
    public static void main(String[] args) {
        // 创建一个 Gson 实例
        Gson gson = new Gson();

        // 1. 把 Java 对象转成 JSON 字符串
        String[] fruits = {"苹果", "香蕉", "橘子"};
        String json = gson.toJson(fruits);
        System.out.println(json);    // 输出：["苹果","香蕉","橘子"]

        // 2. 把 JSON 字符串转回 Java 对象
        String jsonStr = "[1, 2, 3]";
        int[] nums = gson.fromJson(jsonStr, int[].class);
        System.out.println(nums[0]); // 输出：1
    }
}