// 练习 2 辅助类：学生类
// 属性：姓名 name、分数 score
// 要求：构造器、getter、toString

public class Student {

    // TODO: 在这里写属性
    private String name;
    private double score;

    // TODO: 在这里写构造器
    public Student(String name, double score) {
        this.name = name;
        this.score = score;
    }

    // TODO: 在这里写 getter 方法
    public String getName() {
        return name;
    }
    public double getScore() {
        return score;
    }

    // TODO: 在这里写 toString 方法
    public String toString() {
        return "Student [name=" + name + ", score=" + score + "]";
    }
}
