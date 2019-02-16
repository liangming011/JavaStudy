package agent.dynamicagent.javaexample1;

import agent.staticagent.example1.User;
import agent.staticagent.example1.UserManager;
import agent.staticagent.example1.UserManagerImpl;

public class Main {

    public static void main(String[] args) {


        User user = new User();
        user.setUsername("aaa");
        user.setPassword("bbb");


        ObjectProxy1 objectProxy = new ObjectProxy1();
        //java的动态代理只能是接口，Cglib可以直接对类进行代理
        UserManager userManager = (UserManager) objectProxy.createProxy(new UserManagerImpl());

        userManager.addUser(user);
    }
}
