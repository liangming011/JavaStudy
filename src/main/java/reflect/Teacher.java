package reflect;

public class Teacher {

    /****************成员方法**************/

    public void show1(String s) {
        System.out.println("调用了：公有的，String参数在show1：s = " + s);
    }

    private String show2(String s) {
        System.out.println("调用了：私有的，String参数在show1：s = " + s);
        return "123";
    }

    protected void show3() {
        System.out.println("调用了：受保护的，无参数");
    }

    void show4(String s) {
        System.out.println("调用了：默认的，String参数在show1：s = " + s);
    }
}
