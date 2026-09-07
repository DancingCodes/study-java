class Cat extends Animal {
    Cat(String name) {
        super(name);
    }

    void speak() {
        System.out.println(name + "：喵喵~");
    }
}