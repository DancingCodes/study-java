public class Main {

    // ============================
    // 练习 1：字符串统计器
    // 知识点：String API、流程控制、方法
    // ============================
    // 写一个方法 analyzeString(String text)，接收一个字符串，打印以下信息：
    // - 字符串长度
    // - 大写字母个数
    // - 小写字母个数
    // - 数字个数
    // - 其他字符个数
    //
    // 示例：analyzeString("Hello World 123!")
    // 输出：
    //   长度：16
    //   大写字母：2
    //   小写字母：8
    //   数字：3
    //   其他字符：3

    // TODO: 在这里写 analyzeString 方法
    public static void analyzeString(String text) {
        int textLen = text.length();
        long upper = text.chars().filter(Character::isUpperCase).count();
        long lower = text.chars().filter(Character::isLowerCase).count();
        long digit = text.chars().filter(Character::isDigit).count();

        System.out.println("长度：" + textLen);
        System.out.println("大写字母：" + upper);
        System.out.println("小写字母：" + lower);
        System.out.println("数字：" + digit);
        System.out.println("其他字符：" + (textLen - upper - lower - digit));
    }


    // ============================
    // 练习 2：自定义异常 + 简易银行账户
    // 知识点：异常处理、自定义异常、封装、方法
    // ============================
    // 1. 创建 InsufficientBalanceException 异常类（继承 Exception），放在单独文件中
    //    构造器接收两个参数：当前余额 balance 和取款金额 amount
    //    getMessage() 返回："余额不足！当前余额：xxx 元，取款金额：xxx 元"
    //
    // 2. 创建 BankAccount 类，放在单独文件中，包含：
    //    - 属性：户主姓名 ownerName、余额 balance
    //    - 方法：deposit(double amount) 存款（金额必须大于 0）
    //    - 方法：withdraw(double amount) 取款（余额不足时抛出 InsufficientBalanceException）
    //    - 方法：getInfo() 返回 "户主：xxx，余额：xxx 元"
    //
    // 3. 在下面的 testBankAccount() 方法中测试：
    //    - 创建账户（"张三"，初始余额 1000）
    //    - 存入 500
    //    - 取出 2000（应触发异常，用 try-catch 捕获并打印异常信息）
    //    - 打印最终账户信息

    // TODO: 在这里写 testBankAccount 方法
    public static void testBankAccount() {
        BankAccount bankAccount = new BankAccount("张三", 1000);
        bankAccount.deposit(500);
        try {
            bankAccount.withdraw(2000);
        } catch (InsufficientBalanceException e) {
            System.out.println(e.getMessage());
        }
        bankAccount.getInfo();
    }


    public static void main(String[] args) {
        System.out.println("===== 练习 1：字符串统计器 =====");
        // TODO: 调用 analyzeString("Hello World 123!")
        analyzeString("Hello World 123!");

        System.out.println();
        System.out.println("===== 练习 2：银行账户 =====");
        // TODO: 调用 testBankAccount()
        testBankAccount();
    }
}
