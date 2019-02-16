package 枚举;

enum TestEnum3 {

    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);

    TestEnum3(String color, int j) {
        this.color = color;
        this.j = j;
    }

    //覆盖方法
    @Override
    public String toString() {
        return this.j + "_" + this.color;
    }

    private String color;

    private int j;

}
