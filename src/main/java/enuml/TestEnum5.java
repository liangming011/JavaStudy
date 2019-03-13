package enuml;

public interface TestEnum5 {

    enum Coffee implements TestEnum5 {
        BLACK_COFFEE, DECAF_COFFEE, LATTE, CAPPUCCINO
    }

    enum Dessert implements TestEnum5 {
        FRUIT, CAKE, GELATO
    }

}

class SuperTest implements TestEnum5 {

    public static void main1() {
        for (TestEnum5.Dessert dessertEnum : TestEnum5.Dessert.values()) {
            System.out.print(dessertEnum + "  ");
        }
        System.out.println();
        //我这地方这么写，是因为我在自己测试的时候，把这个coffee单独到一个文件去实现那个food接口，而不是在那个接口的内部。
        for (Coffee coffee : Coffee.values()) {
            System.out.print(coffee + "  ");
        }
        System.out.println();
        //搞个实现接口，来组织枚举，简单讲，就是分类吧。如果大量使用枚举的话，这么干，在写代码的时候，就很方便调用啦。
        //还有就是个“多态”的功能吧，
        TestEnum5 food = TestEnum5.Dessert.CAKE;
        System.out.println(food);
        food = Coffee.BLACK_COFFEE;
        System.out.println(food);

    }

}
