import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

class CollectionDemo {
    public static void main(String[] args) {
        ArrayList<String> names = new ArrayList<>();
        names.add("张三");
        names.add("李四");
        System.out.println(names.get(0));
        System.out.println(names.size());
        names.remove("张三");
        System.out.println(names);
        for (String name : names) {
            System.out.println(name);
        }

        // 不可重复，顺序不固定
        HashSet<String> cities = new HashSet<>();
        cities.add("北京");
        cities.add("上海");
        System.out.println(cities);
        System.out.println(cities.size());


        HashMap<String, Integer> scores = new HashMap<>();
        scores.put("张三", 90);
        scores.put("李四", 85);
        System.out.println(scores.get("张三"));
        System.out.println(scores.size());
        System.out.println(scores.containsKey("李四"));
        for (String key : scores.keySet()) {
            System.out.println(key + " → " + scores.get(key));
        }
    }
}