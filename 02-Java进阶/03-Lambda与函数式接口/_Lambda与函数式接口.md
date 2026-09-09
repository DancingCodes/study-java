# Lambda 与函数式接口

## 一、接口、匿名内部类、Lambda 的关系

实现一个接口有三种写法，做的是同一件事，只是越来越简洁（Lambda 仅限只有一个抽象方法的函数式接口）：

```java
// 接口
public interface StringProcessor {
    String process(String input);
}
```

**写法一：实现类（最完整）**
```java
public class UpperProcessor implements StringProcessor {
    @Override
    public String process(String input) {
        return input.toUpperCase();
    }
}
StringProcessor sp = new UpperProcessor();
```

**写法二：匿名内部类（省了类名，不用单独建类）**
```java
StringProcessor sp = new StringProcessor() {
    @Override
    public String process(String input) {
        return input.toUpperCase();
    }
};
```

**写法三：Lambda（省到只剩核心逻辑，只适用于只有一个方法的接口）**
```java
StringProcessor sp = s -> s.toUpperCase();
```

> 三步简化：**实现类 → 匿名内部类 → Lambda**

## 二、回顾匿名内部类

上一阶段写过匿名内部类：

```java
Calculator add = new Calculator() {
    @Override
    public int compute(int a, int b) {
        return a + b;
    }
};
```

写了 5 行，但真正有用的只有 `return a + b` 这一行。太啰嗦了。

## 二、Lambda 表达式

Java 8 引入了 Lambda，用来**简化只有一个抽象方法的接口**的实现：

```java
Calculator add = (a, b) -> a + b;
```

一行搞定。`(a, b) -> a + b` 就是一个 Lambda 表达式。

### 语法

```
(参数列表) -> { 方法体 }
```

简化规则：

| 情况 | 简化 | 示例 |
|------|------|------|
| 方法体只有一行 | 省略 `{}` 和 `return` | `(a, b) -> a + b` |
| 只有一个参数 | 省略 `()` | `x -> x * 2` |
| 参数类型可推断 | 省略类型 | `(a, b)` 而不是 `(int a, int b)` |

### 各种写法对比

```java
// 完整写法
Calculator add = (int a, int b) -> {
    return a + b;
};

// 省略类型（编译器能推断）
Calculator add = (a, b) -> {
    return a + b;
};

// 方法体只有一行，省略 {} 和 return
Calculator add = (a, b) -> a + b;
```

单参数的情况：
```java
// 完整写法
List<String> list = Arrays.asList("banana", "apple", "cherry");
list.sort((String a, String b) -> {
    return a.compareTo(b);
});

// 最简写法
list.sort((a, b) -> a.compareTo(b));
```

无参数的情况：
```java
Runnable task = () -> System.out.println("执行任务");
```

## 三、函数式接口

Lambda 只能用于**函数式接口**——只有一个抽象方法的接口。

```java
@FunctionalInterface  // 加上这个注解，编译器会检查是否只有一个抽象方法
public interface Calculator {
    int compute(int a, int b);
}
```

`@FunctionalInterface` 不是必须的，但建议加上，防止别人往接口里加第二个抽象方法。

### Java 内置的四大函数式接口

Java 在 `java.util.function` 包里提供了常用的函数式接口，不用自己定义：

| 接口 | 方法 | 描述 | 示例 |
|------|------|------|------|
| `Function<T, R>` | `R apply(T t)` | 传入 T，返回 R（转换） | `s -> s.length()` |
| `Consumer<T>` | `void accept(T t)` | 传入 T，无返回（消费） | `s -> System.out.println(s)` |
| `Supplier<T>` | `T get()` | 无参数，返回 T（提供） | `() -> new Student()` |
| `Predicate<T>` | `boolean test(T t)` | 传入 T，返回 boolean（判断） | `n -> n > 0` |

### Function — 转换

```java
import java.util.function.Function;

Function<String, Integer> strToLen = s -> s.length();
int len = strToLen.apply("hello");  // 5

// 链式转换
Function<String, Integer> strToInt = s -> Integer.parseInt(s);
Function<Integer, Integer> doubleIt = n -> n * 2;

int result = strToInt.andThen(doubleIt).apply("5");  // 10
```

### Consumer — 消费

```java
import java.util.function.Consumer;

Consumer<String> printer = s -> System.out.println("输出：" + s);
printer.accept("hello");  // 输出：hello

// 最常见的用法：forEach
List<String> list = List.of("a", "b", "c");
list.forEach(s -> System.out.println(s));
```

### Supplier — 提供

```java
import java.util.function.Supplier;

Supplier<String> greeting = () -> "你好，世界";
System.out.println(greeting.get());  // 你好，世界

// 延迟创建对象
Supplier<List<String>> listFactory = () -> new ArrayList<>();
List<String> list = listFactory.get();
```

### Predicate — 判断

```java
import java.util.function.Predicate;

Predicate<Integer> isPositive = n -> n > 0;
isPositive.test(5);   // true
isPositive.test(-1);  // false

// 组合判断
Predicate<Integer> isEven = n -> n % 2 == 0;
Predicate<Integer> isPositiveEven = isPositive.and(isEven);
isPositiveEven.test(4);   // true
isPositiveEven.test(-2);  // false
isPositiveEven.test(3);   // false
```

## 四、方法引用

当 Lambda 表达式只是调用一个已有的方法时，可以进一步简化：

```java
// Lambda 写法
list.forEach(s -> System.out.println(s));

// 方法引用写法
list.forEach(System.out::println);
```

`::` 就是方法引用，意思是「直接用这个方法」。

### 四种方法引用

| 类型 | 语法 | Lambda 等价 |
|------|------|-------------|
| 静态方法引用 | `类名::方法名` | `s -> Integer.parseInt(s)` → `Integer::parseInt` |
| 实例方法引用 | `对象::方法名` | `s -> System.out.println(s)` → `System.out::println` |
| 特定类型方法引用 | `类名::方法名` | `(s1, s2) -> s1.compareTo(s2)` → `String::compareTo` |
| 构造方法引用 | `类名::new` | `() -> new ArrayList<>()` → `ArrayList::new` |

```java
// 静态方法引用
Function<String, Integer> parse = Integer::parseInt;
parse.apply("123");  // 123

// 构造方法引用
Supplier<List<String>> factory = ArrayList::new;
List<String> list = factory.get();
```

> 方法引用不是必须的，Lambda 能用的地方方法引用也能用，看哪个更清晰就用哪个。

## 五、实际开发中的常见用法

```java
List<String> names = List.of("张三", "李四", "王五", "赵六");

// 遍历
names.forEach(System.out::println);

// 排序
names.sort((a, b) -> a.compareTo(b));
names.sort(String::compareTo);  // 方法引用写法

// 过滤（配合 Stream，下一课学）
names.stream()
     .filter(name -> name.startsWith("张"))
     .forEach(System.out::println);
```

---

## 练习题

### 题目 1：Lambda 基础
- 定义一个函数式接口 `StringProcessor`，有一个方法 `String process(String input)`
- 用 Lambda 分别创建三个实现：转大写、转小写、加前缀 "Hello, "
- 写一个 `execute(StringProcessor sp, String input)` 方法来调用并打印结果

### 题目 2：内置函数式接口
- 用 `Predicate<Integer>` 判断一个数是否是偶数
- 用 `Function<String, Integer>` 把字符串转成它的长度
- 用 `Consumer<String>` 把字符串加上 ">>>" 前缀后打印
- 用 `Supplier<Double>` 生成一个随机数
- 分别调用并打印结果

### 题目 3：方法引用
- 创建一个 `List<String>`，添加几个名字
- 用方法引用 `System.out::println` 遍历打印
- 用方法引用 `String::compareTo` 排序后再打印
