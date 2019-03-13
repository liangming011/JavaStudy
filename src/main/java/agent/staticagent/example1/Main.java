package agent.staticagent.example1;

public class Main {

    public static void main(String[] args) {

        User user = new User();
        user.setPassword("222");
        user.setUsername("000");

        //对实现类进行实例化
        UserManager userManager = new UserManagerImpl();

        //对代理类进行实例化，将实现类的实例化对象传入代理类中
        UserManager userManagerProxy = new UserManagerProxyImpl(userManager);
        
        //近代理类方法即可
        userManagerProxy.addUser(user);


    }
}
