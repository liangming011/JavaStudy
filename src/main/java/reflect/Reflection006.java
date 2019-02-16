package reflect;

import java.lang.reflect.Method;

/*
 * 反射main方法
 * */
public class Reflection006 {
    /*
     * 获取Teacher类中的main方法，不要与当前的mian方法搞混了
     * */
    public static void main(String[] args) throws Exception {
        try {
            //获取Teacher字节码文件
            Class clazz = Class.forName("reflect.School");
            //获取mian方法
            Method metMain = clazz.getMethod("main", String[].class);//第一个参数是方法名称，第二个参数方法形参类型
            //调用main方法
            //第一个参数,对象类型，因为方法是static静态的，所以为null可以，第二个参数是String数组，这里要注意在jdk1.4时是数组，jdk1.5后是可变参数
            //为何强行转换为Object，我TM也不知道
            metMain.invoke(null, (Object) new String[]{"a", "b", "c"});
            metMain.invoke(null, new Object[]{new String[]{"a", "b", "c"}});

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
