package java8.defaultm;

/**
 * Java8 可以向接口添加默认和静态方法来提供具体实现（默认方法就是一个在接口里面有了一个实现的方法。）
 * */
public class StaticMethod {
    public static void main(String args[]){
        Vehicle1 vehicle = new Car();
        vehicle.print();
    }
}

interface Vehicle1 {
    default void print(){
        System.out.println("我是一辆车!");
    }

    static void blowHorn(){
        System.out.println("按喇叭!!!");
    }
}

interface FourWheeler1 {
    default void print(){
        System.out.println("我是一辆四轮车!");
        Car cat = new Car();
        cat.print();
    }
}

class Car implements Vehicle1, FourWheeler1 {
    public void print(){
        Vehicle1.super.print();
        FourWheeler1.super.print();
        Vehicle1.blowHorn();
        System.out.println("我是一辆汽车!");
    }
}