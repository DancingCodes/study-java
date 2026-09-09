import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public class Main {
    // 练习 1：读取文件内容
    // 用 BufferedReader 逐行读取 data.txt 的内容并打印，同时打印行号
    // 示例输出：
    // 1: Hello Java
    // 2: IO Stream
    // 3: 你好世界
    public static void exercise1() {
        // TODO: 在这里写代码
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            int lineCount = 0;
            // readLine() 读一行，到末尾返回 null
            while ((line = br.readLine()) != null) {
                lineCount ++;
                System.out.println(lineCount + "：" +line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 练习 2：写入文件
    // 用 BufferedWriter 将以下 5 个名字写入 names.txt，每个名字占一行：
    // Alice、Bob、Charlie、David、Eve
    public static void exercise2() {
        // TODO: 在这里写代码
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("names.txt"))) {
            bw.write("Alice");
            bw.newLine(); // 跨平台换行
            bw.write("Bob");
            bw.newLine();
            bw.write("Charlie");
            bw.newLine();
            bw.write("David");
            bw.newLine();
            bw.write("Eve");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 练习 3：文件复制
    // 用缓冲字节流将 data.txt 复制为 data_backup.txt
    // 复制完成后打印 "复制完成"
    public static void exercise3() {
        // TODO: 在这里写代码
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("data.txt"));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("data_backup.txt"))) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            System.out.println("复制完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 练习 4：统计文件信息
    // 读取 data.txt，统计并打印：
    // - 总行数
    // - 总字符数（不含换行符）
    // - 最长的一行内容
    public static void exercise4() {
        // TODO: 在这里写代码
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            int lineCount = 0;
            int lineSizeCount = 0;
            String maxLineCtx = "";
            // readLine() 读一行，到末尾返回 null
            while ((line = br.readLine()) != null) {
                lineCount ++;
                lineSizeCount += line.length();
                if (line.length() > maxLineCtx.length()) {
                    maxLineCtx = line;
                }
            }
            System.out.println(lineCount);
            System.out.println(lineSizeCount);
            System.out.println(maxLineCtx);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 练习 1：读取文件 ===");
        exercise1();

        System.out.println("\n=== 练习 2：写入文件 ===");
        exercise2();

        System.out.println("\n=== 练习 3：文件复制 ===");
        exercise3();

        System.out.println("\n=== 练习 4：统计文件信息 ===");
        exercise4();
    }
}
