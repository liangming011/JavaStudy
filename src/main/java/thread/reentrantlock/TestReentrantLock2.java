package thread.reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock2 implements Runnable {

    private Lock lock = new ReentrantLock();

    private Condition condition1 = lock.newCondition();

    public void run(){
        try {
            //lockInterruptibly()：尝试获取锁。如果当前有别的线程获取了锁，则睡眠。当该函数返回时，有两种可能：
            //a.已经获取了锁
            //b.获取锁不成功，但是别的线程打断了它。则该线程会抛出IterruptedException异常而返回，同时该线程的中断标志会被清除。
            //lock.lockInterruptibly()：如果没有获取锁，并中断了，则立马抛出异常，如果你catch，则在后面的处理中，你需要区分是获取了锁还是没有获取锁！！！不要轻易lock.unlock(),也不要不unlock，则会造成严重后果。

            lock.lockInterruptibly();
            System.out.println(Thread.currentThread().getName() + " get lock");
            Thread.sleep(10000);
            System.out.println(Thread.currentThread().getName() + " finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println(Thread.currentThread().getName() + " InterruptedException");
        }finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName() +" now lock is avalibale");
        }
    }

    public static void main(String[] args)throws InterruptedException {
        Thread i1 = new Thread(new TestReentrantLock2());
        Thread i2 = new Thread(new TestReentrantLock2());

        i1.start();
        Thread.sleep(100);

        i2.start();
        //i2.interrupt();
    }
}
