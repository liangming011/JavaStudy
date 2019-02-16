package reflect;

import java.lang.reflect.Method;
import java.lang.String;

/*
 * 入门程序
 * */

public class Reflection001 {

    //reflect Class.forName("java.lang.String")
    public static void main(String[] args) {
        Class c = null;
        try {
            c = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Method m[] = c.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {
            System.out.println(m[i].toString());
        }

    }
}
