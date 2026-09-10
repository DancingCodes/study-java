// 练习 2 辅助类：银行账户类
// 属性：户主姓名 ownerName、余额 balance
// 方法：deposit(存款)、withdraw(取款)、getInfo(账户信息)

public class BankAccount {

    // TODO: 在这里写属性
    private String ownerName;
    private double balance;

    // TODO: 在这里写构造器（接收姓名和初始余额）
    public BankAccount(String ownerName, double balance) {
        this.ownerName = ownerName;
        this.balance = balance;
    }

    // TODO: 在这里写 deposit 方法（金额必须大于 0，否则打印提示）
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        } else {
            System.out.println("金额不能小于等于0");
        }
    }


    // TODO: 在这里写 withdraw 方法（余额不足时抛出 InsufficientBalanceException）
    public void withdraw(double amount) throws InsufficientBalanceException {
        if (balance >= amount) {
            balance -= amount;
        } else {
            throw new InsufficientBalanceException(balance,amount);
        }
    }

    // TODO: 在这里写 getInfo 方法，返回 "户主：xxx，余额：xxx 元"
    public void getInfo() {
        System.out.println("户主："+ ownerName +"，余额："+ balance +" 元");
    }
}
