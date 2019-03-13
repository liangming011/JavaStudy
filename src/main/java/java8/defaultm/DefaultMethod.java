package java8.defaultm;

/**
 * Java8 可以向接口添加默认和静态方法来提供具体实现（默认方法就是一个在接口里面有了一个实现的方法。）
 * */
public class DefaultMethod implements Vehicle, FourWheeler {
    @Override
    public void print() {
        System.out.println("我是一辆四轮汽车!");
        Vehicle.super.print();
    }
}

interface Vehicle {
    default void print(){
        System.out.println("我是一辆车!");
    }
}

interface FourWheeler {
    default void print(){
        System.out.println("我是一辆四轮车!");
    }
}
