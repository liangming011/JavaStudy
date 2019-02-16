package agent.staticagent.example3;

public class ProxySubject implements Subject {

    private Subject realSubject;//代理类中有 老板的引用。

    public Subject TakeCall() //通过电话联系
    {
        return new RealSubject();
    }

    public void Before() {
        System.out.println("我只是一个代理类，在做事情之前我先声明，接下来的事情跟我无关，我只是受人指使！By---" + getClass());
    }

    public void After() {
        System.out.println("正如事情还没有发生之前讲的一样，我只是个路人，上面做的事情跟我无关，我是受人指使的！ By---" + getClass());
    }


    @Override
    public void subjectShow() {
        // TODO Auto-generated method stub

        Object o = TakeCall();  //代理类接到了一个电话

        if (checked(o)) //检查这个电话是不是老板打过来的
        {
            Before();

            this.realSubject = (Subject) o;
            realSubject.subjectShow();

            After();
        } else {
            System.out.println("不好意思，你权限不够，我帮不了你！");
        }


    }

    boolean checked(Object o)  //权限检查，这年头不是谁都可以冒充老板的
    {
        if (o instanceof RealSubject)
            return true;
        return false;
    }
}
