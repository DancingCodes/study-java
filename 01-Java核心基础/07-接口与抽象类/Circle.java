// 题目 1 + 题目 2：
// 继承抽象类 Shape，实现 area()
// 实现 Drawable 接口，实现 draw() 方法
// draw() 里打印类似 "画一个圆形，半径为 xxx" 的信息

public class Circle extends Shape implements Drawable {
    private double radius;

    public Circle(String var1) {
        super(var1);
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double var1) {
        this.radius = var1;
    }

    @Override
    public double area() {
        return 3.14 * this.radius * this.radius;
    }

    @Override
    public void draw() {
        System.out.println("画一个圆形，半径为" + this.radius);
    }
}
