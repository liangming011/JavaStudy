package java8.lambda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
/**
 *
 * Java 8 方法引用: 方法引用提供了非常有用的语法，可以直接引用已有Java类或对象（实例）的方法或构造器。
 * 与lambda联合使用，方法引用可以使用语言的构造更紧凑简洁，减少冗余代码。
 *
 * 方法引用通过方法的名字来指向一个方法。
 *
 * 方法引用可以使语言的构造更紧凑简洁，减少冗余代码。
 *
 * 方法引用使用一对冒号 :: 。
 *
 * 下面，我们在 Car 类中定义了 4 个方法作为例子来区分 Java 中 4 种不同方法的引用。
 * */
class Car {
    //Supplier是jdk1.8的接口，这里和lamda一起使用了
    public static Car create(final Supplier<Car> supplier) {
        return supplier.get();
    }

    public static void collide(final Car car) {
        System.out.println("Collided " + car.toString());
    }

    public void follow(final Car another) {
        System.out.println("Following the " + another.toString());
    }

    public void repair() {
        System.out.println("Repaired " + this.toString());
    }

    public static void main(String args[]){
        List names = new ArrayList();
        names.add("Google");
        names.add("Runoob");
        names.add("Taobao");
        names.add("Baidu");
        names.add("Sina");

        names.forEach(System.out::println);


        //构造器引用：它的语法是Class::new，或者更一般的Class< TEKV >::new实例如下：
        final Car car = Car.create( Car::new );
        final List< Car > cars = Arrays.asList( car );

        //静态方法引用：它的语法是Class::static_method，实例如下：
        cars.forEach( Car::collide );

        //特定类的任意对象的方法引用：它的语法是Class::method实例如下：
        cars.forEach( Car::repair );

        //特定对象的方法引用：它的语法是instance::method实例如下：
        final Car police = Car.create( Car::new );
        cars.forEach( police::follow );

        String time  = "2019-03-07 09:32:13";

        long asd;
        try {
             asd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(time)
                    .getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}