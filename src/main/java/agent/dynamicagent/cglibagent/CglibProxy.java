package agent.dynamicagent.cglibagent;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    public Object getProxy(Class clazz) {
        //设置需要创建子类的类
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        //通过字节码技术动态创建子类实例
        return enhancer.create();

    }

    //实现MethodInterceptor接口方法
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        /**
         * Object o ：是目标对象  实现类实例
         * method ：目标对象实现方法
         * args：入参参数
         * proxy：代理类实例
         * */
        //代理服务方法
        insertLog(method, args);
        //通过代理类调用父类的方法（原业务逻辑）
        Object obj = proxy.invokeSuper(o, args);

        return obj;
    }

    private void insertLog(Method method, Object[] args) {
        System.out.println("logInfo：【methodName:" + method.getName() + ";args:" + args.toString() + "】");
    }
}
