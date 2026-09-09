import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(getName() + i);
        }
    }
}

class MyRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName() + i);
        }
    }
}


// 练习 3 用到的 Counter 类
// 需要你用 synchronized 保证线程安全
class Counter {
    private int count = 0;

    // TODO: 加上 synchronized
    public synchronized void add() {
        count++;
    }

    public int getCount() {
        return count;
    }
}


public class Main {

    // 练习 1：创建线程
    // 分别用继承 Thread 和实现 Runnable 两种方式创建线程
    // 各打印 5 次 "线程名 + 数字"
    public static void exercise1() {
        // TODO: 方式一 —— 继承 Thread
        MyThread t1 = new MyThread();
        t1.start();
        // TODO: 方式二 —— 实现 Runnable
        Thread t2 = new Thread(new MyRunnable());
        t2.start();
    }

    // 练习 2：模拟倒计时
    // 创建一个线程，从 5 倒数到 1，每隔 1 秒打印一次
    // 最后打印 "时间到！"
    public static void exercise2() {
        Thread t = new Thread(() -> {
            for (int i = 5; i >= 1; i--) {
                System.out.println(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("时间到！");
        });
        t.start();
    }

    // 练习 3：线程安全的计数器
    // 启动 2 个线程，每个线程调用 counter.add() 10000 次
    // 最后打印 count（正确结果应该是 20000）
    // 提示：Counter 类写在上面，需要用 synchronized 保证线程安全
    public static void exercise3() throws InterruptedException {
        // TODO: 在这里写代码

        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.add();
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.add();
            }
        });
        t2.start();

        t1.join();
        t2.join();
        System.out.println("count = " + counter.getCount());
    }

    // 练习 4：线程池
    // 创建一个固定大小为 3 的线程池
    // 提交 5 个任务，每个任务打印 "线程名 + 任务编号"
    // 执行完关闭线程池
    public static void exercise4() {
        // TODO: 在这里写代码
        ExecutorService pool = Executors.newFixedThreadPool(3);
        pool.submit(() -> System.out.println(Thread.currentThread().getName() + " 任务1"));
        pool.submit(() -> System.out.println(Thread.currentThread().getName() + " 任务2"));
        pool.submit(() -> System.out.println(Thread.currentThread().getName() + " 任务3"));
        pool.submit(() -> System.out.println(Thread.currentThread().getName() + " 任务4"));
        pool.submit(() -> System.out.println(Thread.currentThread().getName() + " 任务5"));
        pool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 练习 1：创建线程 ===");
        exercise1();
        Thread.sleep(1000); // 等线程执行完

        System.out.println("\n=== 练习 2：模拟倒计时 ===");
        exercise2();
        Thread.sleep(6000); // 等倒计时完成

        System.out.println("\n=== 练习 3：线程安全计数器 ===");
        exercise3();

        System.out.println("\n=== 练习 4：线程池 ===");
        exercise4();
    }
}