// 题目 1：子类 Circle
// 增加 radius 属性，重写 area() 返回圆的面积（π × r²）

public class Circle extends Shape {
    private double radius;

    public Circle(String name) {
        super(name);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return 3.14 * radius * radius;
    }
}
