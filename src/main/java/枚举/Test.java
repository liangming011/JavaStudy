package 枚举;

import java.util.EnumMap;
import java.util.EnumSet;

public class Test {

    public static void main(String[] args) {

        /**
         * 用法一：定义常量
         * */
        System.out.println("*****用法一：定义常量*****");
        TestEnum1 a = TestEnum1.BLANK;
        System.out.println(a);

        /**
         *用法二：switch
         * */
        System.out.println("*****用法二：switch*****");
        TestEnum1 b = TestEnum1.GREEN;
        switch (b) {
            case BLANK:
                System.out.println(b + b.name() + b.ordinal());
                break;
            case RED:
                System.out.println(b + b.name() + b.ordinal());
                break;
            case GREEN:
                System.out.println(b + b.name() + b.ordinal());
                break;
            case YELLOW:
                System.out.println(b + b.name() + b.ordinal());
                break;
            default:
                System.out.println(b + b.name() + b.ordinal());
                break;
        }

        /**
         * 用法三：向枚举中添加新方法
         * */
        System.out.println("*****用法三：向枚举中添加新方法*****");
        String t = TestEnum2.getColorEnum(3);
        System.out.println(t);


        /**
         * 用法四：覆盖枚举的方法
         * */
        System.out.println("*****用法四：覆盖枚举的方法*****");
        TestEnum3 e = TestEnum3.GREEN;
        System.out.println(e.toString());

        /**
         * 用法五：实现接口
         * */
        System.out.println("*****用法五：实现接口*****");
        TestEnum4 y = TestEnum4.YELLO;
        y.print();
        System.out.println(y.getInfo());

        /**
         * 用法六：使用接口组织枚举
         * */
        System.out.println("*****用法六：使用接口组织枚举*****");
        SuperTest.main1();

        /**
         * 用法七：关于枚举集合的使用
         *
         * java.util.EnumSet和java.util.EnumMap是两个枚举集合.
         *
         * EnumSet保证集合中的元素不重复；EnumMap中的 key是enum类型，而value则可以是任意类型。关于这个两个集合的使用就不在这里赘述，可以参考JDK文档。
         * */
        System.out.println("*****用法七：关于枚举集合的使用*****");

        /**
         **EnumMap示例
         * */
        System.out.println("*****EnumMap示例*****");
        // Create an EnumMap that contains all constants of the Car enum.
        EnumMap cars = new EnumMap(Car.class);
        // Put some values in the EnumMap.
        cars.put(Car.BMW, Car.BMW.getPrice());
        cars.put(Car.AUDI, Car.AUDI.getPrice());
        cars.put(Car.MERCEDES, Car.MERCEDES.getPrice());
        cars.put(Car.RED, Car.RED.getPrice());
        // Print the values of an EnumMap.
        for (Car c : Car.values()) {
            System.out.println(c.name());
        }
        System.out.println(cars.size());
        // Remove a Day object.
        cars.remove(Car.BMW);
        System.out.println("After removing Car.BMW, size: " + cars.size());
        // Insert a Day object.
        cars.put(Car.valueOf("BMW"), Car.BMW.getPrice());
        System.out.println("Size is now: " + cars.size());

        /**
         **EnumSet示例
         * */
        System.out.println("*****EnumSet示例*****");
        // Create an EnumSet that contains all days of the week.
        EnumSet week = EnumSet.allOf(Day.class);
        // Print the values of an EnumSet.
        for (Day d : Day.values())
            System.out.println(d.name());

        System.out.println(week.size());

        // Remove a Day object.
        week.remove(Day.FRIDAY);
        System.out.println("After removing Day.FRIDAY, size: " + week.size());

        // Insert a Day object.
        week.add(Day.valueOf("FRIDAY"));
        System.out.println("Size is now: " + week.size());


        /**
         * 日常使用
         * */
        TestEnum6.forEnum();
        TestEnum6.useEnumInJava();


    }
}
