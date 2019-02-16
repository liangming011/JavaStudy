package thread;

import java.util.concurrent.*;

/**
 * 实现Callable接口，重写call方法。Callable接口与Runnable接口的功能类似，但提供了比Runnable更强大的功能。有以下三点
 * 1）.Callable可以在任务结束后提供一个返回值，Runnable没有提供这个功能。
 * 2）.Callable中的call方法可以抛出异常，而Runnable的run方法不能抛出异常。
 * 3）.运行Callable可以拿到一个Future对象，表示异步计算的结果，提供了检查计算是否完成的方法。
 */
public class MultithreadingCallable implements Callable {

    private String x;

    public MultithreadingCallable(String x1) {
        this.x = x1;
    }

    @Override
    public Object call() throws Exception {
        return new Exception(x);
        //System.out.println("MultithreadingCallable Running "+x);
        //return "true";
    }

    public static void main(String[] args) throws Exception {
        //实例化
        MultithreadingCallable t = new MultithreadingCallable("test1");
        //创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(1);
        //调用线程池线程
        Future<Object> ans = executor.submit(t);
        //获取调用结果
        Object result = ans.get();
        //关闭线程池
        executor.shutdown();
        //输出结果
        System.out.println(result);

        //不使用线程池
        Callable<Object> oneCallable = new MultithreadingCallable("test1");
        //由Callable<Integer>创建一个FutureTask<Integer>对象：
        FutureTask<Object> oneTask = new FutureTask<>(oneCallable);
        //注释：FutureTask<Integer>是一个包装器，它通过接受Callable<Integer>来创建，它同时实现了Future和Runnable接口。
        //由FutureTask<Integer>创建一个Thread对象：
        Thread oneThread = new Thread(oneTask);
        oneThread.start();
        //至此，一个线程就创建完成了。

    }
}
