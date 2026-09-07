//class Animal {
//    String name;
//
//    Animal(String name) {
//        this.name = name;
//    }
//
//    void eat() {
//        System.out.println(name + "吃吃吃");
//    }
//}

abstract class Animal {
    String name;
    Animal(String name) {
        this.name = name;
    }

    // 抽象方法：没有方法体，子类必须重写
    abstract void speak();

    // 普通方法：可以有实现
    void eat() {
        System.out.println(name + "在吃东西");
    }
}