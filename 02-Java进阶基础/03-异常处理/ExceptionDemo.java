class AgeException extends Exception {
    AgeException(String message) {
        super(message);
    }
}

class ExceptionDemo {
    static int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("除数不能为0");
        }
        return a / b;
    }

    static void checkAge(int age) throws AgeException {
        if (age < 0) {
            throw new AgeException("年龄不能为负数");
        }
        System.out.println("年龄：" + age);
    }

    public static void main(String[] args) {
        try {
            int a = 10 / 0;
            System.out.println("这行不会执行");
        } catch (ArithmeticException e) {
            System.out.println("出错了：" + e.getMessage());
        }
        System.out.println("程序继续运行");

        try {
            int[] arr = {1, 2, 3};
            System.out.println(arr[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("数组越界了");
        } catch (Exception e) {
            System.out.println("其他错误：" + e.getMessage());
        }

        try {
            int a = 10 / 0;
        } catch (Exception e) {
            System.out.println("出错了");
        } finally {
            System.out.println("我一定会执行");
        }

        try {
            checkAge(-1);
        } catch (AgeException e) {
            System.out.println(e.getMessage());
        }
    }
}
