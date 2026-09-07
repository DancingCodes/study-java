//一个类只能 extends 一个父类，但可以 implements 多个接口
class Dog extends Animal implements Swimmable {
    Dog(String name){
        super(name);
    }

    void speak() {
        System.out.println(name + "：汪汪！");
    }

    // 实现接口的方法必须加 public
    public void swim() {
        System.out.println(name + "在游泳");
    }
}