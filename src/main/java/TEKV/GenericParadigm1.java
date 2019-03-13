package TEKV;

/**
 * 类型方法
 */
public class GenericParadigm1 {

    /**
     * 泛型方法的基本介绍
     * @param tClass 传入的泛型实参
     * @return TEKV 返回值为T类型
     * 说明：
     *     1）public 与 返回值中间<TEKV>非常重要，可以理解为声明此方法为泛型方法。
     *     2）只有声明了<TEKV>的方法才是泛型方法，泛型类中的使用了泛型的成员方法并不是泛型方法。
     *     3）<TEKV>表明该方法将使用泛型类型T，此时才可以在方法中使用泛型类型T。
     *     4）与泛型类的定义一样，此处T可以随便写为任意标识，常见的如T、E、K、V等形式的参数常用于表示泛型。
     */
    public <T> T genericMethod(Class<T> tClass)throws InstantiationException ,
            IllegalAccessException{
        T instance = tClass.newInstance();
        return instance;
    }

    /**
     * 下面是定义泛型方法的规则：
     * <p>
     * 所有泛型方法声明都有一个类型参数声明部分（由尖括号分隔），该类型参数声明部分在方法返回类型之前（在下面例子中的<E>）。
     * 每一个类型参数声明部分包含一个或多个类型参数，参数间用逗号隔开。一个泛型参数，也被称为一个类型变量，是用于指定一个泛型类型名称的标识符。
     * 类型参数能被用来声明返回值类型，并且能作为泛型方法得到的实际参数类型的占位符。
     * 泛型方法方法体的声明和其他方法一样。注意类型参数只能代表引用型类型，不能是原始类型（像int,double,char的等）。
     */
    //无返回参数
    public static <E> void printArray1(E[] inputArray) {
        for (E elemrnt : inputArray) {
            //System.out.printf(elemrnt+" ");
        }
    }

    //有返回参数
    public static <E> E[] printArray2(E[] inputArray) {
        for (E elemrnt : inputArray) {
            System.out.printf(elemrnt + " ");
        }
        return inputArray;
    }

    /**
     * 有界的类型参数:
     * <p>
     * 可能有时候，你会想限制那些被允许传递到一个类型参数的类型种类范围。例如，
     * 一个操作数字的方法可能只希望接受Number或者Number子类的实例。这就是有界类型参数的目的。
     * <TEKV extends Integer>
     * 要声明一个有界的类型参数，首先列出类型参数的名称，后跟extends关键字，最后紧跟它的上界。
     */
    //有界的类型参数
    public static <T extends Comparable<T>> T maximum(T x, T y, T z) {
        T max = x; // 假设x是初始最大值
        if (y.compareTo(max) > 0) {
            max = y; //y 更大
        }
        if (z.compareTo(max) > 0) {
            max = z; // 现在 z 更大
        }
        return max; // 返回最大对象
    }

    public static void main(String[] args) {
        method(null);
//        Integer[] intArray = {1, 2, 3, 4, 5};
//        Double[] doubleArray = {1.1, 2.2, 3.3, 4.4};
//        Character[] charArray = {'H', 'E', 'L', 'L', 'O'};
//        //泛型方法1
//        printArray1(intArray);
//        printArray1(doubleArray);
//        printArray1(charArray);
//
//        //泛型方法2
//        Integer[] a = printArray2(intArray);
//        Double[] b = printArray2(doubleArray);
//        Character[] c = printArray2(charArray);
//        System.out.printf(a.toString() + "\n" + b.length + "\n" + c.length + "\n");
//
//        //泛型方法3
//        int d = maximum(1, 2, 3);
//        Double e = maximum(1.1, 2.2, 3.3);
//        String f = maximum("red", "orange", "green");
//        System.out.printf(d + "\n" + e + "\n" + f + "\n");
//
//        HashMap map = new HashMap(50,0.75f);
//        System.out.println("map大小： "+tableSizeFor(50));

    }
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }



//    public static  void method(String s){
//        System.out.println("string");
//    }
    public static  void method(double[] dArray ){
        System.out.println("Integer");
    }

    public static  void method(Object o){
        System.out.println("object");
    }
}
