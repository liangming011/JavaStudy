package agent.dynamicagent.javaexample1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/*
* 实现动态代理，利用Proxy.newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h)方法获得代理类，
* loader就是被代理类的类加载器，interfaces 是被代理类实现的接口，它实现了几个接口，就会为其生成几个相应的代理类。
* 此外必须要实现InvocationHandler接口，重写其invoke方法，用java.lang.reflect.Method.invoke()方法调用目标方法（可以在调用之前或者之后添加代理服务方法）。
*
*
*
* 动态代理类不仅简化了编程工作，而且提高了软件系统的可扩展性，因为Java反射机制可以生成任意类型的动态代理类。
* java.lang.reflect 包中的Proxy类和InvocationHandler接口提供了生成动态代理类的能力。
    Proxy类提供了创建动态代理类及其实例的静态方法。
* */

public class ObjectProxy1 implements InvocationHandler {

    //被代理的目标类
    private Object targetObject;

    //创建代理
    public Object createProxy(Object targetObject) {
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(), targetObject.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        // 参数proxy制定动态代理类实例，
        // 参数method制定被调用的方法，
        // 参数args制定被调用党阀的参数，
        // invoke()方法的返回值便是被调用方法的返回值
        //System.out.println("proxy:"+proxy);
        //代理服务方法
        insertLog(method, args);
        //调用目标方法（原业务逻辑）
        Object obj = method.invoke(targetObject, args);

        return obj;
    }

    private void insertLog(Method method, Object[] args) {
        System.out.println("logInfo：【methodName:" + method.getName() + ";args:" + args.toString() + "】");
    }

}
