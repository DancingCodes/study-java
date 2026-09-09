import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 已提供的 @MyTest 注解，不用改
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface MyTest {
    String value() default "";
}
