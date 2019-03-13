package enuml;

enum TestEnum2 {

    //枚举添加方法，只能是  RED("Object"，int)这种类型

    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);

    TestEnum2(String color, int i) {
        this.color = color;
        this.i = i;
    }

    public static String getColorEnum(int index) {

        for (TestEnum2 c : TestEnum2.values()) {
            if (c.i == index) {
                return c.color;
            }
        }
        return null;
    }

    private String color;
    private int i;

}
