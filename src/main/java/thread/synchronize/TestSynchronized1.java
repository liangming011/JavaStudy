package thread.synchronize;

public class TestSynchronized1 {

    public synchronized void test1(String name){
        System.out.println(name);
    }

    public static synchronized void test2(String name){
        System.out.println(name);
    }

    public void test3(String name){
        synchronized (name){
            System.out.println(name);
        }

    }

    public static void test4(String name) throws Exception{
        synchronized (name){
            System.out.println(name);
        }

    }




}
