package thread;

public class TestThread01 {

    //获取线程名
    public static void main1(String[] args) {
        MultithreadingRunnable runnable = new MultithreadingRunnable();
        Thread thread = new Thread(runnable, "New Thread");

        thread.start();
        System.out.println(thread.getName());
    }

    //线程代码举例：

    /**
     * 这里是一个小小的例子。首先输出执行main()方法线程名字。这个线程由JVM分配的。然后开启10个线程，命名为1~10。每个线程输出自己的名字后就退出。
     * <p>
     * 需要注意的是，尽管启动线程的顺序是有序的，但是执行的顺序并非是有序的。也就是说，1号线程并不一定是第一个将自己名字输出到控制台的线程。
     * 这是因为线程是并行执行而非顺序的。Jvm和操作系统一起决定了线程的执行顺序，它和线程的启动顺序并非一定是一致的。
     */
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        for (int i = 0; i < 10; i++) {
            new Thread("" + i) {
                public void run() {
                    System.out.println("Thread: " + getName() + " running");
                }
            }.start();
        }
    }
}
