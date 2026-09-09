import java.lang.annotation.*;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)  // 运行时还能读到
@Target(ElementType.METHOD)          // 只能标注在方法上
@interface MyTag {
    String value() default "默认值";
}

class MyService {
    @MyTag("重要方法")
    void doWork() {
        System.out.println("工作中...");
    }

    @MyTag
    void doOther() {
        System.out.println("其他工作...");
    }
}

class AnnotationDemo {
    public static void main(String[] args) {
        Class<?> clazz = MyService.class;

        // 遍历所有方法
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("方法名：" + method.getName());

            // 检查方法上有没有 @MyTag 注解
            if (method.isAnnotationPresent(MyTag.class)) {
                MyTag tag = method.getAnnotation(MyTag.class);
                System.out.println("  标签值：" + tag.value());
            }
        }
    }
}