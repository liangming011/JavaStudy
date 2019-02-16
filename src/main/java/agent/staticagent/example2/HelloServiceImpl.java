package agent.staticagent.example2;

import java.util.Date;

public class HelloServiceImpl implements HelloService {

    @Override
    public String echo(String msg) {
        System.out.println("echo:" + msg);
        return "echo:" + msg;
    }

    @Override
    public Date getTime() {
        return new Date();
    }
}
