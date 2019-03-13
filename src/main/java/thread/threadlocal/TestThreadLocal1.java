package thread.threadlocal;

import thread.reentrantlock.TestReentrantLock2;

public class TestThreadLocal1 implements Runnable {

    private static final ThreadLocal<String> typeThreadLocal = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return "tom";
        }
    };

    public String getName(String name){
        typeThreadLocal.set(name);
        return typeThreadLocal.get();
    }


    public void run(){
        try {
            System.out.println(Thread.currentThread().getName() + typeThreadLocal.get());
            getName(Thread.currentThread().getName()+"alice");
            Thread.sleep(100);
            System.out.println(Thread.currentThread().getName() + typeThreadLocal.get());
        }catch (InterruptedException e){
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName() + " InterruptedException");
        }catch (Exception e1){
            e1.printStackTrace();
        }

    }

    public static void main(String[] args)throws InterruptedException {
        TestThreadLocal1 testThreadLocal1 = new TestThreadLocal1();
        Thread i1 = new Thread(testThreadLocal1);
        Thread i2 = new Thread(testThreadLocal1);


        i1.start();
        Thread.sleep(1000);


        i2.start();

        //i2.interrupt();
    }
}
