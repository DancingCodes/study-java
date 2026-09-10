import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    // ============================
    // 练习 1：学生成绩排名
    // 知识点：集合、排序、泛型、封装
    // ============================
    // 创建一个 Student 类（姓名 name、分数 score），放在 Student.java 中
    // 写一个方法 rankStudents()，在方法内：
    // 1. 创建一个 ArrayList<Student>，添加以下学生：
    //    ("张三", 88)、("李四", 95)、("王五", 72)、("赵六", 88)、("钱七", 63)
    // 2. 按分数从高到低排序（分数相同按姓名字母顺序）
    // 3. 打印排名，格式：
    //    第1名：李四 - 95分
    //    第2名：张三 - 88分
    //    第3名：赵六 - 88分
    //    ...

    // TODO: 在这里写 rankStudents 方法
    public static void rankStudents() {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("张三", 88));
        studentList.add(new Student("李四", 95));
        studentList.add(new Student("王五", 72));
        studentList.add(new Student("赵六", 88));
        studentList.add(new Student("钱七", 63));

        studentList.sort((o1, o2) -> {
            if (o1.getScore() == o2.getScore()) {
                return o1.getName().compareTo(o2.getName());
            }
            return Double.compare(o2.getScore(), o1.getScore());
        });

        for (int i = 0; i < studentList.size(); i++) {
            Student s = studentList.get(i);
            System.out.println("第" + (i + 1) + "名：" + s.getName() + " - " + (int) s.getScore() + "分");
        }
    }


    // ============================
    // 练习 2：Stream 数据处理
    // 知识点：Lambda、Stream API、集合、方法引用
    // ============================
    // 写一个方法 processOrders()，在方法内：
    // 给定一组订单字符串（每条格式为 "商品名-价格-数量"）：
    //   "手机-2999.0-2", "耳机-199.0-5", "键盘-349.0-3",
    //   "手机壳-29.0-10", "显示器-1899.0-1", "鼠标-89.0-4"
    //
    // 用 Stream 完成以下操作并打印结果：
    // 1. 计算每条订单的小计（价格 × 数量），筛选出小计大于 500 的订单
    // 2. 按小计从高到低排序
    // 3. 输出格式："商品名 | 小计：xxx 元"
    // 4. 最后打印这些订单的总金额
    //
    // 提示：可以先用 split("-") 拆分字符串，再用 Double.parseDouble() 转数字

    // TODO: 在这里写 processOrders 方法
    public static void processOrders() {
        List<String> sList = List.of("手机-2999.0-2", "耳机-199.0-5", "键盘-349.0-3", "手机壳-29.0-10", "显示器-1899.0-1", "鼠标-89.0-4");
        List<String[]> newList = sList.stream()
                .map(i -> {
                    String[] parts = i.split("-");
                    double subtotal = Double.parseDouble(parts[1]) * Integer.parseInt(parts[2]);
                    return new String[]{parts[0], parts[1], parts[2], String.valueOf(subtotal)};
                })
                .filter(i -> (Double.parseDouble(i[3]) > 500)).sorted((a, b) -> Double.compare(Double.parseDouble(b[3]), Double.parseDouble(a[3])))
                .collect(Collectors.toList());

        double total = 0;
        for (String[] item : newList) {
            System.out.println(item[0] + " | 小计：" + item[3] + " 元");
            total += Double.parseDouble(item[3]);
        }
        System.out.println("总金额：" + total + " 元");
    }


    // ============================
    // 练习 3：文件词频统计
    // 知识点：IO 流、集合（HashMap）、Stream、排序、异常处理
    // ============================
    // 写一个方法 countWordFrequency(String filePath)：
    // 1. 读取指定路径的文本文件（使用 BufferedReader）
    // 2. 统计每个单词出现的次数，存入 HashMap<String, Integer>
    //    - 单词用空格分隔，统一转小写，去除标点符号（可以用 replaceAll("[^a-zA-Z]", "")）
    //    - 忽略空字符串
    // 3. 用 Stream 按出现次数从高到低排序，打印前 10 个高频词
    //    输出格式："单词 : 出现 x 次"
    // 4. 正确处理 IO 异常（使用 try-with-resources）
    //
    // 在 main 中调用：countWordFrequency("02-Java进阶/综合练习/sample.txt")

    // TODO: 在这里写 countWordFrequency 方法
    public static void countWordFrequency(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            HashMap<String, Integer> map = new HashMap<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                for (String word : split) {
                    String cleaned = word.toLowerCase().replaceAll("[^a-zA-Z]", "");
                    if (cleaned.isEmpty()) {
                        continue;
                    }
                    map.merge(cleaned, 1, Integer::sum);
                }
            }
            List<String> keyList = new ArrayList<>(map.keySet());
            keyList.sort((a,b) -> map.get(b) - map.get(a));
            for (int i = 0; i < Math.min(10, keyList.size()); i++)   {
                System.out.println(keyList.get(i) + " : 出现 " + map.get(keyList.get(i)) + " 次");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println("===== 练习 1：学生成绩排名 =====");
        // TODO: 调用 rankStudents()
        rankStudents();

        System.out.println();
        System.out.println("===== 练习 2：Stream 数据处理 =====");
        // TODO: 调用 processOrders()
        processOrders();

        System.out.println();
        System.out.println("===== 练习 3：文件词频统计 =====");
        // TODO: 调用 countWordFrequency("02-Java进阶/综合练习/sample.txt")
        countWordFrequency("02-Java进阶/综合练习/sample.txt");
    }
}
