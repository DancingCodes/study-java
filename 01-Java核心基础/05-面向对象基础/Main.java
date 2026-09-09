public class Main {
    // 练习：使用 Student 类和 Calculator 类
    // - 用有参构造器创建一个学生，打印学生信息
    // - 尝试用 setter 设置非法的 age 和 score，观察校验效果
    // - 创建 Calculator 对象，调用四个方法并打印结果
    // TODO: 在这里写代码
    public static void main(String[] args) {
        Student student = new Student("张三", 18, 80);
        System.out.println(student.getName() + ", " + student.getAge() + ", " + student.getScore());
        student.setAge(-1);
        student.setScore(-1);

        Calculator calculator = new Calculator();
        System.out.println(calculator.add(1, 2));
        System.out.println(calculator.subtract(1, 2));
        System.out.println(calculator.multiply(1, 2));
        System.out.println(calculator.divide(1, 2));
    }
}
