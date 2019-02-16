package reflect;

import jdk.internal.util.xml.PropertiesDefaultHandler;

import java.io.FileReader;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

/*
 * 通过反射运行配置文件内容
 *
 *   利用反射和配置文件，可以使：应用程序更新时，对源码无需任何更改
 *   我们只需要将新类发送给客户端，并修改配置文件即可
 *   这个配置文件可以使txt文件
 * */
public class Reflection007 {

    public static void main(String[] args) throws Exception {
        //通过反射获取Class对象
        Class clazz = Class.forName(getValue("className"));
        //获取show2方法
        Method m = clazz.getDeclaredMethod(getValue("methodName"), String.class);
        //解除私有限制
        m.setAccessible(true);
        //对show2方法进行调用
        Object obj = clazz.getConstructor().newInstance();
        System.out.println(m.invoke(obj, "tony"));

    }

    public static String getValue(String key) throws Exception {
        Properties pro = new Properties();//创建配置文件的对象
        //获取输入流(这种方式必须本机的物理路径,或文件名和src平级，不是在src目录下)
        //FileReader in = new FileReader("E:\\Self-Study\\IntelliJIDEA\\JavaStudy\\JavaStudy001\\src\\reflect\\test.properties");
        FileReader in = new FileReader("test.properties");

        pro.load(in);//将配置文件输入流放入配置文件对象中去
        in.close();//关闭流操作
        return pro.getProperty(key);//通过key获取value并返回
    }
}
