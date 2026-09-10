@Component
public class OrderDao {
    public void createOrder(String item) {
        System.out.println("创建订单：" + item);
    }
}
