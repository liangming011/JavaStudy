package enuml;

public class TestEnum6 {

    /**
     * 循环枚举,输出ordinal属性；若枚举有内部属性，则也输出。(说的就是我定义的TYPE类型的枚举的typeName属性)
     */
    static void forEnum() {
        for (SimpleEnum simpleEnum : SimpleEnum.values()) {
            System.out.println(simpleEnum + "  ordinal  " + simpleEnum.ordinal());
        }
        System.out.println("------------------");
        for (ComplexEnum type : ComplexEnum.values()) {
            System.out.println("type = " + type + "    type.name = " + type.name() + "   des = " + type.getDes() + "   ordinal = " + type.ordinal());
        }
    }

    /**
     * 在Java代码使用枚举
     */
    static void useEnumInJava() {
        String typeName = "RED";
        ComplexEnum type = ComplexEnum.getComplexEnum(typeName);
        if (ComplexEnum.RED.equals(type)) {
            System.out.println("根据字符串获得的枚举类型实例跟枚举常量一致" + type.getDes());
        } else {
            System.out.println("大师兄代码错误");
        }

    }


    /**
     * 季节枚举(不带参数的枚举常量)这个是最简单的枚举使用实例
     * Ordinal 属性，对应的就是排列顺序，从0开始。
     */
    private enum SimpleEnum {
        SPRING, SUMMER, AUTUMN, WINTER
    }

    /**
     * 常用类型(带参数的枚举常量，这个只是在书上不常见，实际使用还是很多的，看懂这个，使用就不是问题啦。)
     */
    private enum ComplexEnum {
        RED("这是红色"),
        YELLOW("这是黄色"),
        GREEN("这是绿色");

        private String des;

        ComplexEnum(String des) {
            this.des = des;
        }

        public String getDes() {
            return des;
        }

        public static ComplexEnum getComplexEnum(String des) {

            for (ComplexEnum type : ComplexEnum.values()) {
                if (type.name().equals(des)) {
                    return type;
                }
            }
            return null;
        }

    }
}

