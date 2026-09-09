import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.List;
import java.util.ArrayList;


public class Main {

    public static void execute(StringProcessor sp, String input) {
        System.out.println(sp.process(input));
    }


    public static void main(String[] args) {
        // 练习 1：Lambda 基础
        // - 用 Lambda 分别创建三个 StringProcessor 实现：转大写、转小写、加前缀 "Hello, "
        // - 写一个 execute(StringProcessor sp, String input) 方法来调用并打印结果
        StringProcessor toUpper = s -> s.toUpperCase();
        StringProcessor toLower = s -> s.toLowerCase();
        StringProcessor addPrefix = s -> "Hello, " + s;
        execute(toUpper, "hello");
        execute(toLower, "Hello");
        execute(addPrefix, "World");


        // 练习 2：内置函数式接口
        // - 用 Predicate<Integer> 判断一个数是否是偶数
        // - 用 Function<String, Integer> 把字符串转成它的长度
        // - 用 Consumer<String> 把字符串加上 ">>>" 前缀后打印
        // - 用 Supplier<Double> 生成一个随机数
        // - 分别调用并打印结果

        Predicate<Integer> isEven = n -> n % 2 == 0;
        Function<String, Integer> strToLen = s -> s.length();
        Consumer<String> printer = s -> System.out.println(">>>" + s);
        Supplier<Double> greeting = () -> Math.random();


        System.out.println(isEven.test(2));
        System.out.println(strToLen.apply("1"));
        printer.accept("hello");
        System.out.println(greeting.get());

        // 练习 3：方法引用
        // - 创建一个 List<String>，添加几个名字
        // - 用方法引用 System.out::println 遍历打印
        // - 用方法引用 String::compareTo 排序后再打印
        List<String> list = new ArrayList<>();
        list.add("张三");
        list.add("张四");
        list.add("张五");

        list.forEach(System.out::println);
        list.sort(String::compareTo);
        System.out.println(list);
    }
}
