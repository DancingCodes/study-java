class ArrayAndString {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 5};

        int[] scores = new int[3];
        scores[0] = 1;
        scores[1] = 2;
        scores[2] = 3;

        System.out.println(numbers[0]);
        System.out.println(scores[0]);

        for (int i = 0; i < scores.length; i++) {
            System.out.println(scores[i]);
        }

        String name = "code";
        String greeting = name + "!!!";
        System.out.println(greeting);
        System.out.println(greeting.length());
        System.out.println(greeting.charAt(0));


        String s = "Hello World";
        s.equals("hello world");
        s.equalsIgnoreCase("hello world");
        s.contains("Hello");
        s.startsWith("Hello");
        s.substring(0, 5);
        s.replace("World", "Java");
        s.toUpperCase();
        s.toLowerCase();
        s.trim();


        String a = new String("hello");
        String b = new String("hello");
        System.out.println(a == b);
        System.out.println(a.equals(b));

    }
}