import java.lang.annotation.*;

// 标记一个字段需要自动注入
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
}
