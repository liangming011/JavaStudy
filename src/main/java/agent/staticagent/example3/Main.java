package agent.staticagent.example3;

/*
 *
 * 警匪片大家一定都不会陌生，一些有钱的人看那个不顺眼，就想着找黑帮的帮忙杀人，黑帮就帮他们做一些坏事。这里的老板就变成了RealSubject,黑帮就变成了（Proxy），这里的real和proxy只是针对杀人是谁指使的（即幕后黑手是那个）
 *
 *   首先定义一个共同的接口，使得RealSubject出现的地方Proxy都可以出现：创建Subject接口
 *
 *   然后定义一个RealSubject，真正的幕后黑手:创建RealSubject实现类
 *
 *   然后定义一个代理类，黑帮，拿钱办事，但不是幕后黑手:创建ProxySubject代理类
 *
 *   测试Main
 * */

public class Main {

    public static void main(String[] args) {
        ProxySubject proxy = new ProxySubject();

        proxy.subjectShow();
    }
}
