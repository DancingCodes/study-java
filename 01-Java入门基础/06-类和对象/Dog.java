class Dog {
    String name;
    int age;

    Dog(){}

    Dog(String name,int age) {
        this.name = name;
        this.age = age;
    }

    void bark() {
        System.out.println("大狗叫");
    }

    void info() {
        System.out.println(name + age + "岁");
    }
}