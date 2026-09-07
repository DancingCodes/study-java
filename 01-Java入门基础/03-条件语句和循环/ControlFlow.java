class ControlFlow {
    public static void main(String[] args) {
        int score = 85;
        if (score >= 90) {
            System.out.println("优秀");
        } else if (score >= 60) {
            System.out.println("及格");
        } else {
            System.out.println("不及格");
        }

        int day = 3;
        switch (day) {
            case 1:
                System.out.println("1");
                break;
            case 3:
                System.out.println("3");
                break;
            default:
                System.out.println("其他");
        }

        for (int i = 0; i < 5; i++) {
            System.out.println(i);
        }

        int count = 0;
        while (count < 3) {
            System.out.println(count);
            count++;
        }


        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                continue;
            }
            if (i == 5) {
                break;
            }
            System.out.println(i);
        }
    }
}