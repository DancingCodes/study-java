import java.util.List;
import java.util.ArrayList;


public class Main {
    // 题目 2：泛型方法
    // 写一个泛型方法 <T> T getFirst(List<T> list)
    // - 返回列表的第一个元素
    // - 如果列表为空，返回 null
    // - 分别用 List<String> 和 List<Integer> 测试
    //
    public static <T> T getFirst(List<T> list) {
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public static void printAll(List<?> list) {
        for (Object o : list) {
            System.out.println(o);
        }
    }


    public static void main(String[] args) {
        // 题目 1：在 Main 中分别创建 Result<String> 和 Result<Integer> 测试
        Result<String> r1 = new Result<>(true, "ok", "1");
        Result<Integer> r2 = new Result<>(true, "ok", 1);

        List<String> list1 = new ArrayList<>();
        list1.add("hello");
        list1.add("world");
        List<Integer> list2 = new ArrayList<>();
        getFirst(list1);
        getFirst(list2);

        // 题目 3：通配符
        // 写一个方法 printAll(List<?> list)
        // - 遍历打印列表中所有元素
        // - 分别传入 List<String>、List<Integer>、List<Double> 测试
        List<String> list3 = new ArrayList<>();
        List<Integer> list4 = new ArrayList<>();
        List<Double> list5 = new ArrayList<>();
        printAll(list3);
        printAll(list4);
        printAll(list5);
    }
}
