package io.netty.POJOByteBuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

    public static void main(String[] args) throws Exception {

        String host = "127.0.0.1";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //Bootstrap类似于ServerBootstrap它，但它适用于非服务器通道，如客户端或无连接通道。
            Bootstrap b = new Bootstrap(); // (1)

            //如果只指定一个EventLoopGroup，它将同时用作boss组和worker组。但是，老板工作者不会用于客户端。
            b.group(workerGroup); // (2)

            //而不是NioServerSocketChannel，NioSocketChannel正被用于创建客户端Channel。
            b.channel(NioSocketChannel.class); // (3)

            //请注意，我们不使用childOption()此处，ServerBootstrap因为客户端SocketChannel没有父级。
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)

            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeEncoder())
                            .addLast(new TimeDecoder())
                            .addLast(new TimeClientHandler());
                }
            });

            // Start the client. 我们应该调用connect()方法而不是bind()方法。
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            // 等到连接关闭。
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
