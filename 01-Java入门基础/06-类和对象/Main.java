class Main {
    public static void main(String[] args) {
        Dog d1 = new Dog();
        d1.name = "阿黄";
        d1.age = 18;
        d1.bark();
        d1.info();

        Dog d2 = new Dog("小黑",1);
        d2.bark();
        d2.info();
    }
}