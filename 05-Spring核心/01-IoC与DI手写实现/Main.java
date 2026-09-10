/**
 * 练习 1：手写 IoC 容器
 *
 * 对照 md 手敲一遍以上所有类，确保理解每一行，然后运行 Main 看结果。
 * 预期输出：保存用户：张三
 *
 * 练习 2：扩展练习
 *
 * TODO: 新增 OrderDao 类（加 @Component），包含 createOrder(String item) 方法
 * TODO: 新增 OrderService 类（加 @Component），用 @Inject 注入 OrderDao，包含 placeOrder(String item) 方法
 * TODO: 在下方 main 中注册 OrderDao 和 OrderService，调用 placeOrder("Java教程") 测试
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // 1. 创建容器
        SimpleContainer container = new SimpleContainer();

        // 2. 注册 Bean
        container.register(UserDao.class, UserService.class);
        container.register(OrderDao.class, OrderService.class);

        // 3. 从容器获取对象（不再自己 new）
        UserService service = container.getBean(UserService.class);
        service.register("张三");

        // === 练习 2：在这里测试 OrderService ===
        // TODO: 获取 OrderService 并调用 placeOrder
        OrderService orderService = container.getBean(OrderService.class);
        orderService.placeOrder("Java教程");
    }
}
