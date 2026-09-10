// 练习 2 辅助类：余额不足异常
// 继承 Exception
// 构造器接收：当前余额 balance、取款金额 amount
// getMessage() 返回："余额不足！当前余额：xxx 元，取款金额：xxx 元"

public class InsufficientBalanceException extends Exception {

    // TODO: 在这里写属性
    private double balance;
    private double amount;

    // TODO: 在这里写构造器
    public InsufficientBalanceException(double balance, double amount) {
        this.balance = balance;
        this.amount = amount;
    }

    // TODO: 在这里重写 getMessage 方法
    @Override
    public String getMessage() {
        return "余额不足！当前余额："+ balance +" 元，取款金额："+ amount +" 元";
    }
}
