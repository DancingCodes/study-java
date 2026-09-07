class Methods {
    // 方法不能写在main里边
    static void sayHello() {
        System.out.println("hello");
    }

    static int add(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        sayHello();
        int result = add(1, 2);
        System.out.println(result);
    }
}