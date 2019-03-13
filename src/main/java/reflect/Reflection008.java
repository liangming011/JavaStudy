package reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;

/*
 * 通过反射越过泛型检查
 *
 * 例如：有一个String泛型的集合，怎么能向这个集合中添加Inerger类型的值
 *
 * */
public class Reflection008 {

    public static void main(String[] args) throws Exception {

        ArrayList<String> strArray = new ArrayList<>();
        strArray.add("aaa");
        strArray.add("bbb");

        //strArray.add(100);
        //获取ArrayList的Class对象，反向的调用add()方法，添加数据
        Class clazzList = strArray.getClass();//得到strList对象的字节码对象
        //获取add方法
        Method m = clazzList.getDeclaredMethod("add", Object.class);
        //调用add方法
        m.invoke(strArray, 100);

        //遍历集合
        for (Object s : strArray) {
            System.out.println(s.getClass().getName());
        }
    }
}
