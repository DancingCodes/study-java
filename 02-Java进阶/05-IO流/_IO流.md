# IO 流

## 一、什么是 IO？

IO 就是 **Input（输入）** 和 **Output（输出）**，指程序和外部之间的数据传输：
- **输入**：从外部读数据到程序（比如读文件内容）
- **输出**：从程序写数据到外部（比如把内容写进文件）

> 记忆方式：站在程序的角度看，数据进来是 Input，数据出去是 Output。

## 二、流的分类

### 按数据单位分

| 类型 | 单位 | 适用场景 | 核心类 |
|------|------|----------|--------|
| 字节流 | byte（1字节） | 所有文件（图片、视频、压缩包等） | InputStream / OutputStream |
| 字符流 | char（2字节） | 纯文本文件（.txt、.java、.csv） | Reader / Writer |

> 规律：文本文件用字符流更方便（自动处理编码），其他文件必须用字节流。

### 按方向分

| 方向 | 说明 | 类名关键词 |
|------|------|------------|
| 输入流 | 读数据 | InputStream、Reader |
| 输出流 | 写数据 | OutputStream、Writer |

### 按功能分

| 类型 | 说明 | 例子 |
|------|------|------|
| 节点流 | 直接连接数据源 | FileInputStream、FileWriter |
| 处理流（包装流） | 包装节点流，增强功能 | BufferedReader、BufferedWriter |

## 三、四大基类

Java IO 的核心是四个抽象类：

```
            字节流              字符流
输入流    InputStream          Reader
输出流    OutputStream         Writer
```

所有具体的流类都继承自这四个基类。

## 四、字节流

### FileInputStream — 读文件

```java
import java.io.FileInputStream;
import java.io.IOException;

public class Demo {
    public static void main(String[] args) {
        // try-with-resources：自动关闭流，不用手动 close
        try (FileInputStream fis = new FileInputStream("test.txt")) {
            int data;
            // read() 每次读一个字节，读完返回 -1
            while ((data = fis.read()) != -1) {
                System.out.print((char) data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

**一次读一个字节太慢**，可以用 byte 数组批量读：

```java
try (FileInputStream fis = new FileInputStream("test.txt")) {
    byte[] buffer = new byte[1024]; // 每次读 1024 字节
    int len; // 实际读到的字节数
    while ((len = fis.read(buffer)) != -1) {
        // 注意：用 len 而不是 buffer.length，避免多读
        System.out.print(new String(buffer, 0, len));
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

### FileOutputStream — 写文件

```java
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo {
    public static void main(String[] args) {
        // 第二个参数 true 表示追加模式，不写或 false 表示覆盖
        try (FileOutputStream fos = new FileOutputStream("output.txt", true)) {
            fos.write("Hello IO!".getBytes());
            fos.write("\n".getBytes()); // 换行
            fos.write("第二行内容".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### 字节流复制文件

这是字节流最典型的应用 —— 复制任意类型的文件：

```java
try (FileInputStream fis = new FileInputStream("source.jpg");
     FileOutputStream fos = new FileOutputStream("copy.jpg")) {
    byte[] buffer = new byte[1024];
    int len;
    while ((len = fis.read(buffer)) != -1) {
        fos.write(buffer, 0, len);
    }
    System.out.println("复制完成");
} catch (IOException e) {
    e.printStackTrace();
}
```

## 五、字符流

字节流读中文可能会乱码（一个中文占多个字节），字符流自动按字符为单位读写，专门处理文本。

### FileReader — 读文本

```java
import java.io.FileReader;
import java.io.IOException;

try (FileReader fr = new FileReader("test.txt")) {
    char[] buffer = new char[1024];
    int len;
    while ((len = fr.read(buffer)) != -1) {
        System.out.print(new String(buffer, 0, len));
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

### FileWriter — 写文本

```java
import java.io.FileWriter;
import java.io.IOException;

try (FileWriter fw = new FileWriter("output.txt")) {
    fw.write("你好，IO流！\n");
    fw.write("这是字符流写入的内容");
} catch (IOException e) {
    e.printStackTrace();
}
```

## 六、缓冲流（重点）

缓冲流在内部维护一个缓冲区（默认 8KB），减少实际读写磁盘的次数，**性能大幅提升**。

| 节点流 | 对应的缓冲流 |
|--------|-------------|
| FileInputStream | BufferedInputStream |
| FileOutputStream | BufferedOutputStream |
| FileReader | BufferedReader |
| FileWriter | BufferedWriter |

### BufferedReader — 按行读文本（最常用）

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

try (BufferedReader br = new BufferedReader(new FileReader("test.txt"))) {
    String line;
    // readLine() 读一行，到末尾返回 null
    while ((line = br.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

> `readLine()` 是 BufferedReader 特有的方法，非常实用。

### BufferedWriter — 写文本

```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"))) {
    bw.write("第一行");
    bw.newLine(); // 跨平台换行
    bw.write("第二行");
} catch (IOException e) {
    e.printStackTrace();
}
```

### 缓冲流复制文件（推荐方式）

```java
try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("source.jpg"));
     BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("copy.jpg"))) {
    byte[] buffer = new byte[1024];
    int len;
    while ((len = bis.read(buffer)) != -1) {
        bos.write(buffer, 0, len);
    }
    System.out.println("复制完成");
} catch (IOException e) {
    e.printStackTrace();
}
```

## 七、try-with-resources

流用完必须关闭，否则会占用系统资源。Java 7 引入了 `try-with-resources`，在 try 的括号里声明流对象，**结束时自动关闭**：

```java
// 推荐写法：自动关闭
try (FileReader fr = new FileReader("test.txt")) {
    // 使用 fr
} catch (IOException e) {
    e.printStackTrace();
}

// 老写法：手动关闭（了解即可）
FileReader fr = null;
try {
    fr = new FileReader("test.txt");
    // 使用 fr
} catch (IOException e) {
    e.printStackTrace();
} finally {
    if (fr != null) {
        try { fr.close(); } catch (IOException e) { e.printStackTrace(); }
    }
}
```

> 实际开发中一律用 try-with-resources，简洁又安全。

---

## 练习

### 练习 1：读取文件内容
用 BufferedReader 逐行读取 `data.txt` 的内容并打印，同时打印行号。

示例输出：
```
1: Hello Java
2: IO Stream
3: 你好世界
```

### 练习 2：写入文件
用 BufferedWriter 将以下 5 个名字写入 `names.txt`，每个名字占一行：
Alice、Bob、Charlie、David、Eve

### 练习 3：文件复制
用缓冲字节流将 `data.txt` 复制为 `data_backup.txt`，复制完成后打印 "复制完成"。

### 练习 4：统计文件信息
读取 `data.txt`，统计并打印：
- 总行数
- 总字符数（不含换行符）
- 最长的一行内容
