@Component
public class OrderService {
    @Inject
    private OrderDao orderDao;

    public void placeOrder(String item) {
        orderDao.createOrder(item);
    }
}
