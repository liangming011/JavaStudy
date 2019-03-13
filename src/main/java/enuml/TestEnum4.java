package enuml;


interface Behaviour {
    void print();

    String getInfo();
}

public enum TestEnum4 implements Behaviour {

    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);

    TestEnum4(String color, int j) {
        this.color = color;
        this.j = j;
    }

    private String color;

    private int j;


    @Override
    public void print() {
        System.out.println(this.j + ":" + this.color);
    }

    @Override
    public String getInfo() {
        return this.color;
    }
}
