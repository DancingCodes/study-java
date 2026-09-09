// 题目 1：用抽象类改造 Shape
// 把 Shape 改成抽象类，area() 改成抽象方法

public abstract class Shape {
    private String name;

    public Shape(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double area();
}
