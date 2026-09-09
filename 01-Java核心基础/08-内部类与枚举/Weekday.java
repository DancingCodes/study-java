// 题目 2：枚举
// 定义一个枚举 Weekday，包含周一到周日
// 每个枚举值有一个中文名属性（如 "星期一"）
// 提示：参考文档中 Season 的写法，加构造器和 getName() 方法

public enum Weekday {
    Monday("周一"),
    Tuesday("周二"),
    Wednesday("周三"),
    Thursday("周四"),
    Friday("周五"),
    Saturday("周六"),
    Sunday("周日");

    private final String name;
    Weekday(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
