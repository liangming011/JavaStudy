package thread.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock1 implements Runnable {

    private Lock lock = new ReentrantLock();

    private Condition condition1 = lock.newCondition();

    private Condition condition2 = lock.newCondition();

    public void run(){
        //lock.lock()：尝试获取锁。当该函数返回时，处于已经获取锁的状态。如果当前有别的线程获取了锁，则睡眠。
        //lock.lock方法线程被中断时，是该线程已经获取了锁，在检查中断标志，发现自己被人中断了，则抛出异常，因此在finally中一定要释放锁。

        try {

            lock.lock();
            System.out.println(Thread.currentThread().getName() + " get lock");
            Thread.sleep(5000);
            System.out.println(Thread.currentThread().getName() + " finished");
        }catch (InterruptedException e){
            //e.printStackTrace();
            System.out.println(Thread.currentThread().getName() + " InterruptedException");

        }finally {

                lock.unlock();
                System.out.println(Thread.currentThread().getName() +" now lock is avalibale");


        }

    }


    public static void main(String[] args) throws InterruptedException{

        Thread i1 = new Thread(new TestReentrantLock1());
        Thread i2 = new Thread(new TestReentrantLock1());

        i1.start();
        Thread.sleep(100);

        i2.start();
        //i2.interrupt();
        //Thread.sleep(100);
        //i2.interrupt();
        //testReentrantLock1.after("alice");
    }



}
