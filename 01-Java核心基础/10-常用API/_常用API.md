# 常用 API

API（Application Programming Interface）就是 Java 提供的现成的类和方法，拿来就用。这节课学最常用的几个。

## 一、Object 类

所有类的祖宗，每个类都继承了 Object 的方法。

### toString()

默认打印的是 `类名@哈希值`（上一课踩过的坑），重写后可以打印有意义的内容：

```java
public class Student {
    private String name;
    private int age;

    // 重写 toString
    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + "}";
    }
}

Student s = new Student("张三", 18);
System.out.println(s);  // Student{name='张三', age=18}
```

> IDEA 快捷键：`Alt + Insert` → 选 `toString()` 可以自动生成。

### equals()

默认比较的是**内存地址**（和 `==` 一样），重写后可以按内容比较：

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;                  // 同一个对象
    if (obj == null || getClass() != obj.getClass()) return false;  // 类型不同
    Student other = (Student) obj;
    return age == other.age && name.equals(other.name);  // 按内容比较
}
```

> IDEA 快捷键：`Alt + Insert` → 选 `equals() and hashCode()` 自动生成。

### hashCode()

返回对象的哈希码（整数）。规则：`equals` 相等的两个对象，`hashCode` 必须相同。重写 `equals` 时必须同时重写 `hashCode`。

## 二、String 类

Java 中最常用的类，没有之一。

### String 的特点

1. **不可变**：String 对象一旦创建，内容不能修改
2. 每次拼接、替换都会**创建新对象**

```java
String s = "hello";
s = s + " world";  // 不是修改 s，而是创建了一个新的 "hello world"，旧的 "hello" 被丢弃
```

### 创建方式的区别

```java
String s1 = "hello";          // 字符串常量池（复用）
String s2 = "hello";          // 指向同一个对象
String s3 = new String("hello"); // 堆内存（新对象）

System.out.println(s1 == s2);      // true（同一个对象）
System.out.println(s1 == s3);      // false（不同对象）
System.out.println(s1.equals(s3)); // true（内容相同）
```

> **比较字符串内容永远用 `equals()`，不要用 `==`**。

### 常用方法

| 方法 | 作用 | 示例 |
|------|------|------|
| `length()` | 长度 | `"hello".length()` → 5 |
| `charAt(i)` | 取第 i 个字符 | `"hello".charAt(1)` → 'e' |
| `substring(begin)` | 截取从 begin 到末尾 | `"hello".substring(2)` → "llo" |
| `substring(begin, end)` | 截取 [begin, end) | `"hello".substring(1, 3)` → "el" |
| `indexOf(str)` | 查找子串位置 | `"hello".indexOf("ll")` → 2 |
| `contains(str)` | 是否包含 | `"hello".contains("ell")` → true |
| `startsWith(str)` | 是否以...开头 | `"hello".startsWith("he")` → true |
| `endsWith(str)` | 是否以...结尾 | `"hello".endsWith("lo")` → true |
| `toUpperCase()` | 转大写 | `"hello".toUpperCase()` → "HELLO" |
| `toLowerCase()` | 转小写 | `"HELLO".toLowerCase()` → "hello" |
| `trim()` | 去除首尾空格 | `" hello ".trim()` → "hello" |
| `replace(old, new)` | 替换 | `"hello".replace("l", "L")` → "heLLo" |
| `split(regex)` | 分割成数组 | `"a,b,c".split(",")` → ["a", "b", "c"] |
| `isEmpty()` | 是否为空串 | `"".isEmpty()` → true |
| `equals(str)` | 内容比较 | `"hello".equals("hello")` → true |
| `equalsIgnoreCase(str)` | 忽略大小写比较 | `"Hello".equalsIgnoreCase("hello")` → true |

## 三、StringBuilder

String 每次拼接都创建新对象，大量拼接时性能差。StringBuilder 是**可变的**字符串，拼接不创建新对象：

```java
StringBuilder sb = new StringBuilder();
sb.append("hello");
sb.append(" ");
sb.append("world");
System.out.println(sb.toString());  // hello world
```

### 链式调用

```java
String result = new StringBuilder()
    .append("姓名：").append("张三")
    .append("，年龄：").append(18)
    .toString();
// 姓名：张三，年龄：18
```

### String vs StringBuilder

| | String | StringBuilder |
|--|--------|---------------|
| 可变性 | 不可变 | 可变 |
| 拼接性能 | 差（每次创建新对象） | 好（在原对象上修改） |
| 线程安全 | 安全（不可变） | 不安全 |
| 使用场景 | 普通使用 | 大量拼接（循环拼接、拼 SQL 等） |

> 还有一个 `StringBuffer`，和 StringBuilder 一样，但线程安全。实际开发中几乎只用 StringBuilder。

## 四、Math 类

数学工具类，方法全是 static，直接 `Math.xxx()` 调用：

```java
Math.abs(-10);       // 10（绝对值）
Math.max(3, 7);      // 7（最大值）
Math.min(3, 7);      // 3（最小值）
Math.pow(2, 3);      // 8.0（2 的 3 次方）
Math.sqrt(16);       // 4.0（平方根）
Math.ceil(3.2);      // 4.0（向上取整）
Math.floor(3.8);     // 3.0（向下取整）
Math.round(3.5);     // 4（四舍五入）
Math.random();       // 0.0 ~ 1.0 之间的随机数
Math.PI;             // 3.141592653589793
```

### 生成指定范围随机数

```java
// 生成 1~100 的随机整数
int random = (int)(Math.random() * 100) + 1;
```

## 五、Arrays 类

数组工具类：

```java
import java.util.Arrays;

int[] arr = {5, 3, 1, 4, 2};

// 排序
Arrays.sort(arr);  // [1, 2, 3, 4, 5]

// 转字符串
System.out.println(Arrays.toString(arr));  // [1, 2, 3, 4, 5]

// 二分查找（数组必须有序）
int index = Arrays.binarySearch(arr, 3);  // 2（下标）

// 填充
int[] arr2 = new int[5];
Arrays.fill(arr2, 0);  // [0, 0, 0, 0, 0]

// 复制
int[] copy = Arrays.copyOf(arr, 3);  // [1, 2, 3]（取前 3 个）
```

## 六、日期时间（Java 8+ 新 API）

旧的 `Date` 和 `Calendar` 设计很烂，Java 8 引入了全新的日期时间 API，推荐使用。

### LocalDate — 日期（年月日）

```java
import java.time.LocalDate;

LocalDate today = LocalDate.now();          // 2026-09-09
LocalDate date = LocalDate.of(2026, 1, 15); // 2026-01-15

date.getYear();       // 2026
date.getMonthValue(); // 1
date.getDayOfMonth(); // 15

date.plusDays(10);    // 2026-01-25（加 10 天，返回新对象）
date.minusMonths(1);  // 2025-12-15（减 1 个月）

date.isAfter(today);  // 比较日期
date.isBefore(today);
```

### LocalTime — 时间（时分秒）

```java
import java.time.LocalTime;

LocalTime now = LocalTime.now();           // 14:30:15.123
LocalTime time = LocalTime.of(14, 30, 0);  // 14:30:00

time.getHour();   // 14
time.getMinute(); // 30
```

### LocalDateTime — 日期 + 时间

```java
import java.time.LocalDateTime;

LocalDateTime now = LocalDateTime.now();  // 2026-09-09T14:30:15
now.toLocalDate();  // 取日期部分
now.toLocalTime();  // 取时间部分
```

### DateTimeFormatter — 格式化

```java
import java.time.format.DateTimeFormatter;

LocalDateTime now = LocalDateTime.now();
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

String str = now.format(fmt);  // "2026-09-09 14:30:15"

// 字符串 → 日期
LocalDateTime parsed = LocalDateTime.parse("2026-09-09 14:30:15", fmt);
```

## 七、包装类补充

之前学过基本类型对应的包装类，这里补充几个常用方法：

```java
// 字符串 → 数字
int num = Integer.parseInt("123");       // 123
double d = Double.parseDouble("3.14");   // 3.14

// 数字 → 字符串
String s1 = String.valueOf(123);         // "123"
String s2 = 123 + "";                    // "123"（简写）

// 进制转换
Integer.toBinaryString(10);  // "1010"（二进制）
Integer.toHexString(255);    // "ff"（十六进制）
```

---

## 练习

### 练习 1：String 练习
给定一个字符串 `"  Hello, World! Java is Great!  "`：
- 去除首尾空格
- 转成全小写
- 把 "great" 替换成 "awesome"
- 用 "," 分割，打印每一段（注意分割后每段可能有前导空格，也 trim 一下）

### 练习 2：StringBuilder 练习
用 StringBuilder 拼接 1 到 100，用逗号分隔：`"1,2,3,...,100"`
注意最后一个数字后面不要有逗号。

### 练习 3：日期练习
- 获取今天的日期，打印年、月、日
- 计算 100 天后是几月几号
- 把日期格式化成 `"yyyy年MM月dd日"` 的格式打印
