package agent.dynamicagent.specialexample1;


import java.lang.reflect.Proxy;

/**
 * 一个叫做zake的java程序员，在开发的某个时候会祈祷一下，这样他的代码就不会有bug。
 */
public class JavaDynamicProxy {

    public static void main(String[] args) {

        //一个叫做zake1的java程序员
        JavaDeveloper zake = new JavaDeveloper("zake");

        //这是java程序员的日常祈祷时间
        Developer zakeProxy = (Developer) Proxy.newProxyInstance(zake.getClass().getClassLoader(),
                zake.getClass().getInterfaces(), (proxy, method, agrs) -> {
                    if (method.getName().equals("code")) {
                        System.out.println("zake is praying for the code !");
                        return method.invoke(zake, agrs);
                    }
                    if (method.getName().equals("debug")) {
                        System.out.println("zake is praying for the debug !");
                        return null;
                    }
                    return null;
                });
        //通过日常祈祷code和debug
        zakeProxy.code();
        zakeProxy.debug();

    }

    /**
     * loder，选用的类加载器。因为代理的是zack，所以一般都会用加载zack的类加载器。
     * interfaces，被代理的类所实现的接口，这个接口可以是多个。
     * h，绑定代理类的一个方法。
     *
     * InvocationHandler作用就是，当代理对象的原本方法被调用的时候，会绑定执行一个方法，
     * 这个方法就是InvocationHandler里面定义的内容，同时会替代原本方法的结果返回。
     *
     * InvocationHandler接收三个参数
     *
     * proxy，代理后的实例对象。
     * method，对象被调用方法。
     * args，调用时的参数。
     *
     * invoke的对象不是zack，而是proxy，根据上面的说明猜猜会发生什么？
     * 是的，会不停地循环调用。因为proxy是代理类的对象，当该对象方法被调用的时候，会触发InvocationHandler，
     * 而InvocationHandler里面又调用一次proxy里面的对象，所以会不停地循环调用。并且，proxy对应的方法是没有实现的.所以是会循环的不停报错
     * */
}
