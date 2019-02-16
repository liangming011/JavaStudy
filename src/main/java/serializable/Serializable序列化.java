package serializable;

import java.io.*;

/**
 * 序列化测试
 *
 * @author lxk on 2017/11/1
 */
public class Serializable序列化 {

    public static void main(String[] args) throws Exception {
        serializeFlyPig();
        FlyPig flyPig = deserializeFlyPig();
        System.out.println(flyPig.toString());

    }

    /**
     * 序列化
     * <p>
     * ObjectOutputStream代表对象输出流：
     * <p>
     * 它的writeObject(Object obj)方法可对参数指定的obj对象进行序列化，把得到的字节序列写到一个目标输出流中。
     */
    private static void serializeFlyPig() throws IOException {
        FlyPig flyPig = new FlyPig();
        flyPig.setColor("black");
        flyPig.setName("naruto");
        flyPig.setCar("0000");
        // ObjectOutputStream 对象输出流，将 flyPig 对象存储到E盘的 flyPig.txt 文件中，完成对 flyPig 对象的序列化操作
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("classpath:flyPig.txt")));
        oos.writeObject(flyPig);
        System.out.println("FlyPig 对象序列化成功！");
        oos.close();

    }

    /**
     * 反序列化
     * <p>
     * ObjectInputStream代表对象输入流：
     * <p>
     * 它的readObject()方法从一个源输入流中读取字节序列，再把它们反序列化为一个对象，并将其返回。
     */
    private static FlyPig deserializeFlyPig() throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("classpath:flyPig.txt")));
        FlyPig person = (FlyPig) ois.readObject();
        System.out.println("FlyPig 对象反序列化成功！");
        return person;
    }


    /**
     * 第一种：上来就这些代码，不动，直接run，看效果。
     * 运行结果：
     *
     * 1，他实现了对象的序列化和反序列化。
     *
     * 2，transient 修饰的属性，是不会被序列化的。我设置的奥迪四个圈的车不见啦，成了null。my god。
     *
     * 3，你先别着急说，这个静态变量AGE也被序列化啦。这个得另测。
     */


    /**
     * 第二种：为了验证这个静态的属性能不能被序列化和反序列化，可如下操作。
     *  //FlyPig flyPig = deserializeFlyPig();
     *  //System.out.println(flyPig.toString());
     *
     * 这个完了之后，意思也就是说，你先序列化个对象到文件了。这个对象是带静态变量的static。
     *
     * 现在修改flyPig类里面的AGE的值，给改成26吧。
     *
     * 然后，看下图里面的运行代码和执行结果。
     *
     * 可以看到，刚刚序列化的269，没有读出来。而是刚刚修改的26，如果可以的话，应该是覆盖这个26，是269才对。
     *
     * 所以，得出结论，这个静态static的属性，他不序列化。
     *
     */

    /**
     *
     * 第三种：示范这个 serialVersionUID 的作用和用法
     *
     * 最暴力的改法，直接把model的类实现的这个Serializable接口去掉。然后执行后面的序列化和反序列化的方法。直接报错。
     *
     * 抛异常：NotSerializableException
     *
     * 这个太暴力啦，不推荐这么干。
     *
     * 然后就是，还和上面的操作差不多，先是单独执行序列化方法。生成文件。
     * 然后，打开属性 addTip ，这之后，再次执行反序列化方法，看现象。
     *
     * 抛异常：InvalidClassException  详情如下。
     *
     * InvalidClassException: com.lxk.model.FlyPig;
     * local class incompatible:
     * stream classdesc serialVersionUID = -3983502914954951240,
     * local class serialVersionUID = 7565838717623951575
     *
     * 解释一下：
     *
     * 因为我再model里面是没有明确的给这个 serialVersionUID 赋值，但是，Java会自动的给我赋值的，
     *
     * 这个值跟这个model的属性相关计算出来的。
     *
     * 我保存的时候，也就是我序列化的时候，那时候还没有这个addTip属性呢，
     *
     * 所以，自动生成的serialVersionUID 这个值，
     *
     * 在我反序列化的时候Java自动生成的这个serialVersionUID值是不同的，他就抛异常啦。
     *
     * （你还可以反过来，带ID去序列化，然后，没ID去反序列化。也是同样的问题。）
     *
     *
     *
     * 再来一次，就是先序列化，这个时候，把 private static final long serialVersionUID = 1L; 这行代码的注释打开。那个addTip属性先注释掉
     *
     * 序列化之后，再把这个属性打开，再反序列化。看看什么情况。
     */


    /**
     *
     * 这个现象对我们有什么意义：
     *
     * 老铁，这个意义比较大，首先，你要是不知道这个序列化是干啥的，万一他真的如开头所讲的那样存数据库啦，socket传输啦，rmi传输啦。虽然我也不知道这是干啥的。
     * 你就给model bean 实现了个这个接口，你没写这个 serialVersionUID 那么在后来扩展的时候，可能就会出现不认识旧数据的bug，那不就炸啦吗。
     * 回忆一下上面的这个出错情况。想想都可怕，这个锅谁来背？
     *
     * 所以，有这么个理论，就是在实现这个Serializable 接口的时候，一定要给这个 serialVersionUID 赋值，就是这么个问题。
     *
     * 这也就解释了，我们刚刚开始编码的时候，实现了这个接口之后，为啥eclipse编辑器要黄色警告，需要添加个这个ID的值。而且还是一长串你都不知道怎么来的数字。
     *
     *
     * 下面解释这个 serialVersionUID 的值到底怎么设置才OK。
     *
     * 首先，你可以不用自己去赋值，Java会给你赋值，但是，这个就会出现上面的bug，很不安全，所以，还得自己手动的来。
     *
     * 那么，我该怎么赋值，eclipse可能会自动给你赋值个一长串数字。这个是没必要的。
     *
     * 可以简单的赋值个 1L，这就可以啦。。这样可以确保代码一致时反序列化成功。
     *
     * 不同的serialVersionUID的值，会影响到反序列化，也就是数据的读取，你写1L，注意L大写。
     *
     * 计算机是不区分大小写的，但是，作为观众的我们，是要区分1和L的l，所以说，这个值，闲的没事不要乱动，
     * 不然一个版本升级，旧数据就不兼容了，你还不知道问题在哪。。。
     * */

    /**
     * 下面是摘自 jdk api 文档里面关于接口 Serializable 的描述
     *
     * 类通过实现 java.io.Serializable 接口以启用其序列化功能。
     *
     * 未实现此接口的类将无法使其任何状态序列化或反序列化。
     * 可序列化类的所有子类型本身都是可序列化的。
     * 因为实现接口也是间接的等同于继承。
     * 序列化接口没有方法或字段，仅用于标识可序列化的语义。
     * */


}
