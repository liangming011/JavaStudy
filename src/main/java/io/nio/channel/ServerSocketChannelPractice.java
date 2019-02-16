package io.nio.channel;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Java NIO中的 ServerSocketChannel 是一个可以监听新进来的TCP连接的通道,
 * 就像标准IO中的ServerSocket一样。ServerSocketChannel类在 java.nio.channels包中。
 * */
public class ServerSocketChannelPractice {

    //这里有个例子：
    public static void main(String[] args) {

        try {
            //创建测试SocketChannel
            socketChannelTest();

            /**
             * 打开 ServerSocketChannel
             * 通过调用 ServerSocketChannel.open() 方法来打开ServerSocketChannel.如：
             * */
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            /**
             * 监听新进来的连接
             * 通过 ServerSocketChannel.accept() 方法监听新进来的连接。
             * 当 accept()方法返回的时候,它返回一个包含新进来的连接的 SocketChannel。因此, accept()方法会一直阻塞到有新连接到达。
             *
             * 通常不会仅仅只监听一个连接,在while循环中调用 accept()方法. 如下面的例子：
             *
             * 当然,也可以在while循环中使用除了true以外的其它退出准则。
             * */
            serverSocketChannel.socket().bind(new InetSocketAddress(9999));//设置监听端口
            while (true) {
                SocketChannel socketChannel =
                        serverSocketChannel.accept();
                ByteBuffer buf = ByteBuffer.allocate(48);
                socketChannel.read(buf);
                while(buf.hasRemaining()) {
                    System.out.println((char)buf.get() );
                }

                //do something with socketChannel...

            }

            /**
             * 关闭 ServerSocketChannel
             * 通过调用ServerSocketChannel.close() 方法来关闭ServerSocketChannel. 如：
             * */
//            serverSocketChannel.close();

            /**
             * 非阻塞模式
             * ServerSocketChannel可以设置成非阻塞模式。在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null。
             * 因此，需要检查返回的SocketChannel是否是null.如：
             * */
//            ServerSocketChannel serverSocketChannel0 = ServerSocketChannel.open();
//
//            serverSocketChannel0.socket().bind(new InetSocketAddress(9999));
//            serverSocketChannel0.configureBlocking(false);
//
//            while(true){
//                SocketChannel socketChannel =
//                        serverSocketChannel.accept();
//
//                if(socketChannel != null){
//                    //do something with socketChannel...
//                }
//            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }

    }

    public static void socketChannelTest(){
        try {
            SocketChannel channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("0.0.0.0",9999));

            String newData = "New String to write to file..." + System.currentTimeMillis();

            ByteBuffer buf = ByteBuffer.allocate(48);
            buf.clear();
            buf.put(newData.getBytes());

            buf.flip();

            while(buf.hasRemaining()) {
                channel.write(buf);
            };

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
