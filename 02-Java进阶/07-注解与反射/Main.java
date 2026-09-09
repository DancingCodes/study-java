import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

// 练习 1：自定义注解
// 创建一个 @Info 注解，可以贴在类和方法上，运行时保留
// 有两个属性：
// - author：作者名，无默认值
// - version：版本号，默认值 "1.0"

// TODO: 在这里写 @Info 注解
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@interface Info {
    String author();
    String version() default "1.0";
}



// 练习 2：使用注解
// 创建一个 Calculator 类，包含 add()、subtract()、multiply() 三个方法（简单打印即可）
// 给 add() 和 multiply() 贴上 @MyTest 注解

// TODO: 在这里写 Calculator 类
class Calculator {
    @MyTest
    public static void add(){
        System.out.println("add");
    }
    public static void subtract(){
        System.out.println("subtract");
    }
    @MyTest
    public static void multiply(){
        System.out.println("multiply");
    }
}


public class Main {
    // 练习 3：反射读取注解并执行
    // 用反射遍历 Calculator 的所有方法
    // 找到贴了 @MyTest 的方法并执行，同时打印方法名
    public static void main(String[] args) throws Exception {
        // TODO: 在这里写代码
        Class<?> clazz = Calculator.class;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(MyTest.class)) {
                MyTest annotation = method.getAnnotation(MyTest.class);
                method.invoke(null);
                System.out.println(method.getName());
            }
        }
    }
}
