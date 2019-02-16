package agent.dynamicagent.javaexample2;

import agent.staticagent.example3.Subject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ObjectProxy2 implements InvocationHandler {

    private Object targetObject;

    public Object ObjectProxy2(Object targetObject) {
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(
                Subject.class.getClassLoader(),
                new Class[]{Subject.class},
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("准备工作之前：");

        //转调具体目标对象
        Object obj = method.invoke(targetObject, args);

        System.out.println("准备工作之后：");

        return obj;
    }
}
