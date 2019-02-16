package agent.staticagent.example2;

public class Main {

    public static void main(String[] args) throws Exception {
        HelloService helloService = new HelloServiceImpl();
        HelloService helloServiceProxy = new HelloServiceProxy(helloService);
        helloServiceProxy.echo("tony");
        //System.out.println(helloServiceProxy.getTime());
    }
}
