package reflect;

import reflect.Student;

import java.lang.reflect.Field;

/*
 * 通过反射获取成员变量并调用
 *
 *   1.批量的
 *       1）：field[] getFields():获取多有的“公有字段”
 *       2）：field[] getDeclaredFields():获取所有的字段，包括私有，公有，受保护，默认
 *   2.获取单个的
 *       1）：public Field getField(String fieldName):获取某个“公有的”字段
 *       2）：public Field getDeclaredFiled(String filedName):获取某个字段（可以是私有的）
 *   设置字段的值
 *       Filed --> public void set(Object obj,Object value):
 *               参数说明：
 *                   1）：obj：要设置的字段所在的对象
 *                   2）：value：要为字段设置的值
 * */
public class Reflection004 {

    public static void main(String[] args) throws Exception {
        //获取Class对象
        Class clazz = Class.forName("reflect.Student");

        //获取字段
        System.out.println("************************获取所有公有的字段***************************");
        Field[] filedArray = clazz.getFields();
        for (Field f : filedArray) {
            System.out.println(f);
        }

        //获取字段
        System.out.println("***************获取所有的字段(包括默认的，受保护的，私有的)***************");
        filedArray = clazz.getDeclaredFields();
        for (Field f : filedArray) {
            System.out.println(f);
        }

        //获取字段
        System.out.println("************************获取某个公有的字段并调用***************************");
        Field f = clazz.getField("age");
        System.out.println(f);
        //获取一个对象
        Object obj = clazz.getConstructor().newInstance();//产生Student对象-->Student stu = new Student();
        //为字段设置值
        f.set(obj, 12);//为Student中的age对象赋值-->stu.age = 12
        //验证
        Student stu = (Student) obj;
        System.out.println("验证年龄：" + stu.age);

        //获取字段
        System.out.println("***********获取某个字段并调用(包括默认的，受保护的，私有的)******************");
        f = clazz.getDeclaredField("phoneNum");
        System.out.println(f);
        //获取一个对象
        obj = clazz.getConstructor().newInstance();//产生Student对象-->Student stu = new Student();
        //为字段设置值
        f.setAccessible(true);
        f.set(obj, "13132513236");//为Student中的age对象赋值-->stu.age = 12
        //验证
        stu = (Student) obj;
        System.out.println("验证数据：" + stu);

    }
}
