# Stream API

## 一、什么是 Stream？

Stream 是 Java 8 引入的**数据处理流水线**，可以用简洁的方式对集合进行过滤、转换、排序、统计等操作。

不用 Stream：
```java
List<String> names = List.of("张三", "李四", "张飞", "王五", "张三丰");

// 找出所有姓张的名字，转大写，排序后打印
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.startsWith("张")) {
        result.add(name);
    }
}
result.sort(String::compareTo);
for (String name : result) {
    System.out.println(name);
}
```

用 Stream：
```java
names.stream()
     .filter(name -> name.startsWith("张"))
     .sorted()
     .forEach(System.out::println);
```

三行搞定。Stream 就是把「遍历 + 判断 + 操作」变成**链式调用**。

## 二、Stream 的三步流程

```
创建 Stream → 中间操作（可以多个）→ 终端操作（只能一个，触发执行）
```

### 创建 Stream

```java
// 从集合创建（最常用）
List<String> list = List.of("a", "b", "c");
Stream<String> stream = list.stream();

// 从数组创建
String[] arr = {"a", "b", "c"};
Stream<String> stream = Arrays.stream(arr);

// 直接创建
Stream<String> stream = Stream.of("a", "b", "c");
```

## 三、中间操作

中间操作返回的还是 Stream，可以**链式调用**。中间操作是**惰性的**——不调用终端操作就不会执行。

### filter — 过滤

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);

nums.stream()
    .filter(n -> n % 2 == 0)   // 只保留偶数
    .forEach(System.out::println);  // 2 4 6
```

### map — 转换

把每个元素转换成另一个东西：

```java
List<String> names = List.of("hello", "world");

names.stream()
     .map(s -> s.toUpperCase())     // 每个元素转大写
     .forEach(System.out::println);  // HELLO WORLD

// 方法引用写法
names.stream()
     .map(String::toUpperCase)
     .forEach(System.out::println);
```

### sorted — 排序

```java
List<Integer> nums = List.of(5, 1, 3, 2, 4);

// 自然排序（升序）
nums.stream()
    .sorted()
    .forEach(System.out::println);  // 1 2 3 4 5

// 自定义排序（降序）
nums.stream()
    .sorted((a, b) -> b - a)
    .forEach(System.out::println);  // 5 4 3 2 1
```

### distinct — 去重

```java
List<Integer> nums = List.of(1, 2, 2, 3, 3, 3);

nums.stream()
    .distinct()
    .forEach(System.out::println);  // 1 2 3
```

### limit / skip — 截取 / 跳过

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);

nums.stream().limit(3).forEach(System.out::println);  // 1 2 3（取前 3 个）
nums.stream().skip(2).forEach(System.out::println);   // 3 4 5（跳过前 2 个）
```

### peek — 查看（调试用）

```java
nums.stream()
    .peek(n -> System.out.println("处理：" + n))  // 查看每个元素，不影响流
    .filter(n -> n > 2)
    .forEach(System.out::println);
```

## 四、终端操作

终端操作触发 Stream 执行，执行后 Stream 就不能再用了。

### forEach — 遍历

```java
list.stream().forEach(System.out::println);
```

### collect — 收集成集合（最常用）

```java
import java.util.stream.Collectors;

List<Integer> nums = List.of(1, 2, 3, 4, 5, 6);

// 过滤后收集成新 List
List<Integer> evenList = nums.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());  // [2, 4, 6]

// 收集成 Set
Set<Integer> evenSet = nums.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toSet());

// 收集成字符串
String joined = List.of("a", "b", "c").stream()
    .collect(Collectors.joining(", "));  // "a, b, c"
```

### toList()（Java 16+ 简化写法）

```java
List<Integer> evenList = nums.stream()
    .filter(n -> n % 2 == 0)
    .toList();  // 更简洁，但返回不可变 List
```

### count / min / max / sum

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);

long count = nums.stream().count();  // 5

Optional<Integer> max = nums.stream().max(Integer::compareTo);  // 5
Optional<Integer> min = nums.stream().min(Integer::compareTo);  // 1

// 求和（用 mapToInt 转成 IntStream）
int sum = nums.stream().mapToInt(Integer::intValue).sum();  // 15
```

### anyMatch / allMatch / noneMatch

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);

nums.stream().anyMatch(n -> n > 3);   // true（有任意一个 > 3）
nums.stream().allMatch(n -> n > 0);   // true（全部 > 0）
nums.stream().noneMatch(n -> n < 0);  // true（没有一个 < 0）
```

### reduce — 归约

把所有元素合并成一个结果：

```java
List<Integer> nums = List.of(1, 2, 3, 4, 5);

// 求和
int sum = nums.stream().reduce(0, (a, b) -> a + b);  // 15
// 等价于
int sum = nums.stream().reduce(0, Integer::sum);  // 15
```

## 五、实际开发常见场景

### 场景 1：过滤 + 转换 + 收集

```java
List<Student> students = ...;

// 找出所有及格学生的名字
List<String> passedNames = students.stream()
    .filter(s -> s.getScore() >= 60)
    .map(Student::getName)
    .collect(Collectors.toList());
```

### 场景 2：分组

```java
// 按成绩等级分组
Map<String, List<Student>> groups = students.stream()
    .collect(Collectors.groupingBy(s -> {
        if (s.getScore() >= 90) return "优秀";
        if (s.getScore() >= 60) return "及格";
        return "不及格";
    }));
```

### 场景 3：转 Map

```java
// 学生列表转成 name -> score 的 Map
Map<String, Double> scoreMap = students.stream()
    .collect(Collectors.toMap(Student::getName, Student::getScore));
```

## 六、注意事项

1. **Stream 只能用一次**：终端操作执行后，Stream 就关闭了，不能再调用
2. **不修改原集合**：Stream 操作不会改变原来的 List
3. **惰性求值**：中间操作不会立即执行，只有终端操作触发时才一起执行

---

## 练习题

### 题目 1：基础操作
给定 `List<Integer> nums = List.of(5, 3, 8, 1, 9, 2, 7, 4, 6)`：
- 过滤出大于 5 的数
- 排序（升序）
- 收集成新的 List 并打印

### 题目 2：字符串处理
给定 `List<String> words = List.of("hello", "world", "java", "stream", "hello", "java")`：
- 去重
- 转成大写
- 按字母排序
- 用逗号拼接成一个字符串并打印

### 题目 3：统计
给定 `List<Integer> scores = List.of(85, 92, 78, 95, 60, 45, 88, 72)`：
- 打印及格（>= 60）的人数
- 打印最高分和最低分
- 打印所有及格分数的平均值
