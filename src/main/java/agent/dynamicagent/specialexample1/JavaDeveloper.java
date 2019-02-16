package agent.dynamicagent.specialexample1;

/**
 * 一个java程序员也是程序员，他有一个名字
 */
public class JavaDeveloper implements Developer {

    private String name;

    JavaDeveloper(String name) {
        this.name = name;
    }

    @Override
    public void code() {

        System.out.println(this.name + "is coding java");

    }

    @Override
    public void debug() {

        System.out.println(this.name + "is debuging java");

    }
}
