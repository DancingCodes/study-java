class Box<T> {
    T value;

    Box(T value) {
        this.value = value;
    }

    T getValue() {
        return value;
    }
}



class GenericDemo {
    static <T> void printItem(T item) {
        System.out.println("内容：" + item);
    }
    public static void main(String[] args) {
        // 基本类型不能用泛型
        Box<String> box1 = new Box<>("hello");
        System.out.println(box1.getValue());

        printItem("hello");
    }
}