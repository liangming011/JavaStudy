package agent.staticagent.example1;

/*
 * 代理类
 * */

public class UserManagerProxyImpl implements UserManager {

    private UserManager userManager;

    public UserManagerProxyImpl(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void addUser(User user) {
        userManager.addUser(user);
        //添加额外服务-日志功能
        //Logger.info("添加用户："+user.getUsername());
        System.out.println("添加用户名：" + user.getUsername() + " 密码：" + user.getPassword());
    }
}
