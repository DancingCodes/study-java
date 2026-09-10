import java.lang.annotation.*;

// 标记一个类需要被容器管理
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {
}
