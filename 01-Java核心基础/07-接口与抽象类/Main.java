public class Main {
    // 练习：接口多态测试
    // - 写一个 bindDraw(Drawable d) 方法，调用 d.draw()
    // - 分别创建 Circle 和 Rectangle，调用 bindDraw 测试
    // - 同时也测试 printArea（和上一课一样）
    // TODO: 在这里写代码
    public static void printArea(Shape shape) {
        System.out.println(shape.area());
        if (shape instanceof Circle) {
            System.out.println("圆形");
        }
        if (shape instanceof Rectangle) {
            System.out.println("长方形");
        }
    }

    public static void bindDraw(Drawable d) {
        d.draw();
    }

    public static void main(String[] args) {
        Circle circle = new Circle("圆形");
        circle.setRadius(1);
        Rectangle rectangle = new Rectangle("长方形");
        rectangle.setHeight(2);
        rectangle.setWidth(3);
        printArea(circle);
        printArea(rectangle);
        bindDraw(circle);
        bindDraw(rectangle);
    }
}
