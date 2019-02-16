package agent.dynamicagent.javaexample2;

import agent.staticagent.example3.RealSubject;
import agent.staticagent.example3.Subject;

public class Main {

    public static void main(String[] args) {

        //接口和实现类实例化
        Subject real = new RealSubject();
        ObjectProxy2 objectProxy2 = new ObjectProxy2();
        //调用代理类构造方法
        real = (Subject) objectProxy2.ObjectProxy2(real);
        //调用方法
        real.subjectShow();
    }
}
