// 练习 1 + 练习 2：
// 继承抽象类 Shape，实现 area()
// 实现 Drawable 接口，实现 draw() 方法
// draw() 里打印类似 "画一个长方形，宽 xxx 高 xxx" 的信息

public class Rectangle extends Shape implements Drawable {
    private double width;
    private double height;

    public Rectangle(String name) {
        super(name);
    }

    public double getHeight() {
        return height;
    }
    public void setHeight(double height) {
        this.height = height;
    }
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public double area() {
        return this.getHeight() * this.getWidth();
    }

    @Override
    public void draw() {
        System.out.println("画一个长方形，宽" + this.width + "高" + this.height);
    }
}
