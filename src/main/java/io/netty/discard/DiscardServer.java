package io.netty.discard;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 丢弃任何传入的数据。
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // NioEventLoopGroup是一个处理I/O操作的多线程事件循环。
        // Netty EventLoopGroup为不同类型的传输提供各种实现。
        // 我们在此示例中实现了服务器端应用程序，因此NioEventLoopGroup将使用两个。
        // 第一个，通常称为“老板”，接受传入连接。
        // 第二个，通常称为“工人”，一旦老板接受连接并将接受的连接注册到工作人员，就处理被接受连接的流量。
        // 使用了多少个线程以及它们如何映射到创建的Channels取决于EventLoopGroup实现，甚至可以通过构造函数进行配置。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrap是一个设置服务器的帮助程序类。
            // 您可以Channel直接使用a设置服务器。
            // 但请注意，这是一个繁琐的过程，在大多数情况下您不需要这样做。
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)

                    //在这里，我们指定使用NioServerSocketChannel用于实例化new的类Channel来接受传入连接。
                    .channel(NioServerSocketChannel.class)

                    // 此处指定的处理程序将始终由新接受的计算器进行评估Channel。
                    // 这ChannelInitializer是一个特殊的处理程序，旨在帮助用户配置新的Channel。
                    // 您最有可能希望通过添加一些处理程序（例如实现网络应用程序）来配置ChannelPipeline新Channel
                    // 的处理DiscardServerHandler程序。
                    // 随着应用程序变得复杂，您可能会向管道添加更多处理程序，并最终将此匿名类提取到顶级类中。
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })

                    // 您还可以设置特定于Channel实现的参数。我们正在编写TCP/IP服务器，因此我们可以设置套接字选项，
                    // 如tcpNoDelay和keepAlive。请参阅apidocs ChannelOption和具体ChannelConfig实现，
                    // 以获得有关支持ChannelOption的概述。
                    .option(ChannelOption.SO_BACKLOG, 128)

                    //你有没有注意到option()和childOption()？
                    // option()是为了NioServerSocketChannel接受传入的连接。
                    // childOption()是Channel父母接受的ServerChannel，NioServerSocketChannel在这种情况下。
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 我们现在准备好了。剩下的就是绑定到端口并启动服务器。
            // 在这里，我们绑定到机器8080中所有NIC（网络接口卡）的端口。
            // 您现在可以bind()根据需要多次调用该方法（使用不同的绑定地址。）
            // 绑定并开始接受传入连接，开启异步。
            ChannelFuture f = b.bind(port).sync(); // (7)

            // 等到服务器套接字关闭。
            // 在这个例子中，这不会发生，但你可以优雅地做到这一点
            // 关闭你的服务器。
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        //telnet localhost 8080
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new DiscardServer(port).run();
    }
}