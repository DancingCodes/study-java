@Component
public class UserService {
    @Inject
    private UserDao userDao;

    public void register(String name) {
        userDao.save(name);
    }
}
