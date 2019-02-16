package thread;

/**
 * 继承Thread类，重写run方法。Thread本质上也是一个实现了Runnable的实例，他代表一个线程的实例，并且启动线程的唯一方法就是通过Thread类的start方法。
 */
public class MultithreadingThread extends Thread {

    @Override
    public void run() {
        System.out.println("MyThread running");
    }

    public static void main(String[] args) {

        MultithreadingThread t = new MultithreadingThread();
        t.start();

        //匿名类
        Thread thread = new Thread() {
            public void run() {
                System.out.println("Thread Running");
            }
        };
        thread.start();
    }
}
