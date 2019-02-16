package reflect;

import java.lang.reflect.Constructor;


/*
 * 通过反射获取构造方法并使用
 * */
public class Reflection003 {
    /*
     *
     *
     * 通过Class对象可以获取某个类中的：构造方法，成员变量，成员方法；并访问成员；
     *
     * 1.获取构造方法：
     *   1）：批量的方法：
     *      public Constructor[] getConstructors();所有'共有的'构造方法
     *      public Constructor[] getDeclaredConstructors();获取所有的构造方法（包括私有，受保护，默认，共有）
     *   2）：获取单个的方法，并调用
     *     public Constructor[] getConstructors(Class... patameterTypes):获取单个的“共有的”构造方法
     *     public Constructor[] getDeclaredConstructors(Class... patameterTypes);获取“某个”的构造方法（包括私有，受保护，默认，共有）
     *
     *   调用构造方法：
     *     Constructor --> newInstance(Object... initargs)
     *
     * */
    public static void main(String[] args) throws Exception {
        //加载Class对象
        Class clazz = Class.forName("reflect.Student");
        //获取所有公有的构造方法
        System.out.println("**********************所有共有的构造方法*************************");
        Constructor[] conArray = clazz.getConstructors();
        for (Constructor c : conArray) {
            System.out.println(c);
        }

        //获取所有的构造方法
        System.out.println("**********************所有的构造方法*************************");
        conArray = clazz.getDeclaredConstructors();
        for (Constructor c : conArray) {
            System.out.println(c);
        }

        //获取所有的构造方法
        System.out.println("**********************获取公有、无参的构造方法*************************");
        //Constructor con = clazz.getConstructor(null);
        Constructor con = clazz.getConstructor(String.class, int.class);
        /*1):因为是无参的构造方法所以类型是一个null，也可以不写：这里需要一个参数类型，切记是类型
          2):返回的是描述这个无参构造函数的类对象
        * */
        System.out.println("con = " + con);
        //调用构造方法
        Object obj = con.newInstance("tony", 12);
        System.out.println("obj = " + obj);

        //获取所有的构造方法
        System.out.println("**********************获取私有的构造方法并调用*************************");
        //Constructor con = clazz.getDeclaredConstructor(null);
        con = clazz.getDeclaredConstructor(int.class);

        System.out.println("con = " + con);
        //调用构造方法
        con.setAccessible(true);//暴力访问（忽略掉访问修饰符）访问私有需要执行的操作
        obj = con.newInstance(12);
        System.out.println("obj = " + obj);
    }
}
