// 题目 1：定义一个继承体系
// 父类 Shape：有 name 属性（private + getter），有 area() 方法返回 0.0

public class Shape {
    private String name;

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double area() {
        return 0.0;
    }
}
