package agent.staticagent.example2;

import java.util.Date;

public class HelloServiceProxy implements HelloService {

    private HelloService helloService;

    public HelloServiceProxy(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public String echo(String msg) {

        //预处理业务操作
        System.out.println("echo-->预处理业务操作");

        //调用被代理的HelloService实例echo方法
        String str = helloService.echo(msg);

        //事后业务操作
        System.out.println("echo-->事后业务操作");
        return str;
    }

    @Override
    public Date getTime() {

        //预处理业务操作
        System.out.println("getTime-->预处理业务操作");

        //调用被代理的HelloService实例getTime方法
        Date date = helloService.getTime();

        //事后业务操作
        System.out.println("getTime-->事后业务操作");
        return date;
    }
}
