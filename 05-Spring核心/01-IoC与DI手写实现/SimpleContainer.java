import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SimpleContainer {
    // Bean 仓库：类型 → 实例
    private Map<Class<?>, Object> beanMap = new HashMap<>();

    // 注册 Bean（创建对象并存起来）
    public void register(Class<?>... classes) throws Exception {
        // 第一步：创建所有对象
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class)) {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                beanMap.put(clazz, instance);
            }
        }

        // 第二步：注入依赖
        for (Object bean : beanMap.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    Object dependency = beanMap.get(field.getType());
                    if (dependency != null) {
                        field.set(bean, dependency);
                    }
                }
            }
        }
    }

    // 获取 Bean
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) {
        return (T) beanMap.get(clazz);
    }
}
