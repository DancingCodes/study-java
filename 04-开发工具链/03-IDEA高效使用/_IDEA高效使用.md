# IDEA 高效使用

## 一、为什么要学 IDEA 技巧

工具用得好，效率翻倍。写代码的时间其实只占一小部分，大量时间花在**导航、查找、调试、重构**上。掌握 IDEA 快捷键和功能，能让你把时间花在思考而不是操作上。

## 二、最常用快捷键

### 2.1 搜索导航（最高频）

| 快捷键 | 作用 | 使用场景 |
|--------|------|----------|
| `双击 Shift` | 搜索一切（文件、类、方法、设置） | 不知道在哪就双击 Shift |
| `Ctrl + N` | 搜索类名 | 找某个类 |
| `Ctrl + Shift + N` | 搜索文件名 | 找配置文件、SQL 文件等 |
| `Ctrl + Shift + F` | 全局搜索内容 | 找某段代码、某个字符串 |
| `Ctrl + E` | 最近打开的文件 | 在文件间快速切换 |
| `Ctrl + B` | 跳转到定义 | 看方法/类的源码 |
| `Ctrl + Alt + B` | 跳转到实现 | 接口跳到实现类 |
| `Alt + ←/→` | 切换编辑器标签页 | 多文件间切换 |

### 2.2 编辑代码

| 快捷键 | 作用 | 使用场景 |
|--------|------|----------|
| `Alt + Enter` | 万能修复（最重要！） | 报错、警告、导包、生成代码 |
| `Ctrl + Alt + L` | 格式化代码 | 代码缩进乱了 |
| `Ctrl + D` | 复制当前行 | 快速复制一行 |
| `Ctrl + Y` | 删除当前行 | 快速删一行 |
| `Ctrl + /` | 单行注释 | 注释/取消注释 |
| `Ctrl + Shift + /` | 块注释 | 多行注释 |
| `Alt + Shift + ↑/↓` | 上下移动行 | 调整代码位置 |
| `Ctrl + Alt + V` | 提取变量 | 选中表达式，自动提取成变量 |
| `Ctrl + W` | 扩大选中范围 | 快速选中一个词 → 一行 → 一块 |

### 2.3 生成代码

| 快捷键 | 作用 | 使用场景 |
|--------|------|----------|
| `Alt + Insert` | 生成代码菜单 | 生成 getter/setter/构造器/toString/equals |
| `Ctrl + O` | 重写方法 | 重写父类或接口方法 |
| `Ctrl + I` | 实现方法 | 实现接口的抽象方法 |
| `sout + Tab` | `System.out.println()` | 快速打印 |
| `psvm + Tab` | `public static void main` | 快速生成 main 方法 |
| `fori + Tab` | for 循环 | 快速生成 for(int i=0;...) |
| `iter + Tab` | 增强 for 循环 | 快速生成 for-each |

### 2.4 快捷键记忆口诀

**最重要的 5 个，先记住这些就够了：**
1. `Alt + Enter` — 万能修复
2. `双击 Shift` — 搜索一切
3. `Ctrl + B` — 跳转定义
4. `Ctrl + Alt + L` — 格式化
5. `Alt + Insert` — 生成代码

## 三、代码模板（Live Templates）

输入缩写按 Tab 自动展开：

| 缩写 | 展开结果 |
|------|----------|
| `sout` | `System.out.println();` |
| `psvm` | `public static void main(String[] args) {}` |
| `fori` | `for (int i = 0; i < ; i++) {}` |
| `iter` | `for (Type item : collection) {}` |
| `ifn` | `if (var == null) {}` |
| `inn` | `if (var != null) {}` |
| `thr` | `throw new` |

你也可以自定义模板：Settings → Editor → Live Templates。

## 四、调试（Debug）

调试是找 bug 的核心技能，比到处加 `System.out.println` 高效 100 倍。

### 4.1 基本调试流程

1. **打断点** — 点击行号左侧的空白处，出现红点
2. **Debug 启动** — 右键 → Debug（或点绿色虫子图标）
3. **程序运行到断点处暂停**，你可以查看变量值
4. **单步执行**，一步步看代码怎么跑的

### 4.2 调试按钮

| 按钮/快捷键 | 作用 | 说明 |
|-------------|------|------|
| `F8` | Step Over | 执行当前行，不进入方法内部 |
| `F7` | Step Into | 进入方法内部 |
| `Shift + F8` | Step Out | 跳出当前方法 |
| `F9` | Resume | 继续运行到下一个断点 |
| `Alt + F8` | Evaluate | 运行时计算表达式的值 |

### 4.3 调试技巧

- **条件断点**：右键红点 → 设置条件（如 `i == 5`），只有满足条件才停
- **Watches**：在 Debug 窗口添加监视表达式，实时看某个值的变化
- **Evaluate Expression**（Alt+F8）：暂停时可以临时执行代码，测试某个表达式

### 4.4 什么时候用调试

- 代码结果不对，不知道哪一步出了问题 → 打断点一步步看
- 循环里某次迭代出错 → 条件断点定位到那一次
- 方法调用链很深，想看中间值 → Step Into 进去看

## 五、重构功能

IDEA 的重构不是简单的查找替换，它能理解代码结构，安全地修改。

| 快捷键 | 作用 | 说明 |
|--------|------|------|
| `Shift + F6` | 重命名 | 改变量名/方法名/类名，所有引用自动更新 |
| `Ctrl + Alt + M` | 提取方法 | 选中代码块，抽成一个新方法 |
| `Ctrl + Alt + V` | 提取变量 | 选中表达式，提取为局部变量 |
| `Ctrl + Alt + F` | 提取字段 | 提取为类的成员变量 |
| `Ctrl + Alt + C` | 提取常量 | 提取为 static final 常量 |
| `F6` | 移动 | 把类移到另一个包 |

**最常用的是 `Shift + F6` 重命名**，改名字的时候永远不要手动查找替换，用这个。

## 六、其他实用功能

### 6.1 Postfix Completion

在变量后面输入 `.` 加关键词，自动转换：

| 输入 | 结果 |
|------|------|
| `list.for` | `for (Object o : list) {}` |
| `list.fori` | `for (int i = 0; i < list.size(); i++) {}` |
| `str.null` | `if (str == null) {}` |
| `str.nn` | `if (str != null) {}` |
| `value.sout` | `System.out.println(value);` |
| `value.var` | `Type value = expression;` 自动推断类型 |
| `value.return` | `return value;` |

### 6.2 常用设置

- **自动导包**：Settings → Editor → General → Auto Import → 勾选 Add unambiguous imports on the fly
- **字体大小**：Settings → Editor → Font → Size
- **编码格式**：Settings → Editor → File Encodings → 全部设为 UTF-8

## 练习

### 练习 1：快捷键实操

打开 maven-demo 的 `App.java`，练习以下操作：

1. 用 `sout` 模板快速打印一句话
2. 用 `Ctrl + D` 复制一行
3. 用 `Ctrl + Alt + L` 格式化代码
4. 用 `Shift + F6` 把类名 `App` 改成 `MavenApp`（观察文件名也自动变了）
5. 用 `双击 Shift` 搜索 `pom.xml` 并打开

### 练习 2：调试实操

在 `MavenApp.java`（或 `App.java`）里写以下代码，然后用 Debug 模式运行：

```java
public static void main(String[] args) {
    int sum = 0;
    for (int i = 1; i <= 10; i++) {
        sum += i;
        System.out.println("i=" + i + ", sum=" + sum);
    }
    System.out.println("最终结果：" + sum);
}
```

1. 在 `sum += i;` 这一行打断点
2. Debug 启动，观察 Variables 面板中 `i` 和 `sum` 的值
3. 按 F8 单步执行几次，看值怎么变化
4. 设置条件断点：右键红点，条件设为 `i == 5`，重新 Debug，看是不是直接跳到 i=5

### 练习 3：思考题

1. `Alt + Enter` 能做哪些事情？（至少说 3 个）
2. 调试时 F7 和 F8 的区别是什么？
3. 为什么改名字要用 `Shift + F6` 而不是 `Ctrl + H` 查找替换？
