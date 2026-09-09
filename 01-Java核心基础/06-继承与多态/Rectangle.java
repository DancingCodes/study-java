// 练习 1：子类 Rectangle
// 增加 width 和 height 属性，重写 area() 返回长方形面积

public class Rectangle extends Shape {
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
}
