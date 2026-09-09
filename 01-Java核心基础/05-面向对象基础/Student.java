// 练习 1：定义一个 Student 类
// 属性：name（String）、age（int）、score（double），用 private
// 写有参构造器和无参构造器
// 写 getter 和 setter
// setter 中：age 不能小于 0，score 必须在 0-100 之间

public class Student {
    private String name;
    private int age;
    private double score;

    public Student() {
    }

    public Student(String name, int age, double score) {
        this.name = name;
        this.setAge(age);
        this.setScore(score);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {
            System.out.println("年龄不能小于0");
            return;
        }
        this.age = age;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        if (score < 0 || score > 100) {
            System.out.println("成绩不能小于0并且大于100");
            return;
        }
        this.score = score;
    }
}