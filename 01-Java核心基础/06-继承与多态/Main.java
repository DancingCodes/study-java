public class Main {
    // 练习：多态测试
    // - 写一个 printArea(Shape shape) 方法，接收 Shape 参数，打印形状名称和面积
    // - 分别创建 Circle 和 Rectangle，调用 printArea 测试多态效果
    // - 用 instanceof 判断传入的是哪种形状，打印额外信息
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

    public static void main(String[] args) {
        Circle circle = new Circle("圆形");
        circle.setRadius(1);
        Rectangle rectangle = new Rectangle("长方形");
        rectangle.setHeight(2);
        rectangle.setWidth(3);
        printArea(circle);
        printArea(rectangle);
    }
}
