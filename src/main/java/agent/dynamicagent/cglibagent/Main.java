package agent.dynamicagent.cglibagent;

import agent.staticagent.example1.User;
import agent.staticagent.example1.UserManager;
import agent.staticagent.example1.UserManagerImpl;

public class Main {

    public static void main(String[] args) {


        User user = new User();
        user.setUsername("aaa");
        user.setPassword("bbb");

        CglibProxy objectProxy = new CglibProxy();
        //Cglib可以直接对类进行代理，java的动态代理只能是接口
        UserManagerImpl userManagerimpl = (UserManagerImpl) objectProxy.getProxy(UserManagerImpl.class);

        userManagerimpl.addUser(user);
    }
}
