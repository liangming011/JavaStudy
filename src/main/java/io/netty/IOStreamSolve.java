package io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.time.TimeServerHandler;

import java.util.Date;
import java.util.List;

public class IOStreamSolve {
    private int port;

    private IOStreamSolve(int port) {
        this.port = port;
    }

    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //添加两种解决方式
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                        }
                    })
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeServerHandler());
                        }
                    });

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
        /**
         * 处理基于流的传输
         * 一个小插槽缓冲区
         * 在诸如TCP / IP的基于流的传输中，接收的数据被存储到套接字接收缓冲器中。
         * 不幸的是，基于流的传输的缓冲区不是数据包队列而是字节队列。这意味着，即使您将两条消息作为两个独立的数据包发送，
         * 操作系统也不会将它们视为两条消息，而只是一堆字节。因此，无法保证您所阅读的内容正是您的远程同行所写的内容。
         * 例如，假设操作系统的TCP / IP堆栈已收到三个数据包：
         *
         * 发送时收到三个数据包
         *
         * 由于基于流的协议的这种一般属性，在应用程序中以下面的碎片形式读取它们的可能性很高：
         *
         * 三个数据包拆分并合并为四个缓冲区
         *
         * 因此，接收部分，无论是服务器端还是客户端，都应该将接收到的数据碎片整理成一个或多个有意义的帧，
         * 应用程序逻辑可以很容易地理解这些帧。在上述示例的情况下，接收的数据应如下框架：
         *
         * 四个缓冲区碎片整理为三个
         *
         *
         * 以下提供两种解决方案
         * */
    }


    /**
     * 第一种解决方案
     * <p>
     * 简单的解决方案是创建一个内部累积缓冲区，并等待所有4个字节都被接收到内部缓冲区。以下是TimeClientHandler修复此问题的修改实现：
     * <p>
     * 现在让我们回到TIME客户端示例。我们在这里遇到同样的问题。
     * 32位整数是非常少量的数据，并且不太可能经常被分段。
     * 然而，问题在于它可能是碎片化的，并且随着流量的增加，碎片化的可能性将增加。
     */
    class TimeClientHandler extends ChannelInboundHandlerAdapter {
        private ByteBuf buf;

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            //A ChannelHandler有两个生命周期监听器方法：handlerAdded()和handlerRemoved()。
            // 您可以执行任意（de）初始化任务，只要它不会长时间阻塞。
            buf = ctx.alloc().buffer(4); // (1)
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {
            //A ChannelHandler有两个生命周期监听器方法：handlerAdded()和handlerRemoved()。
            // 您可以执行任意（de）初始化任务，只要它不会长时间阻塞。
            buf.release(); // (1)
            buf = null;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf m = (ByteBuf) msg;
            //首先，应将所有收到的数据累积到buf。
            buf.writeBytes(m); // (2)
            m.release();

            //然后，处理程序必须检查是否buf有足够的数据，在此示例中为4个字节，然后继续执行实际的业务逻辑。
            // 否则，channelRead()当更多数据到达时，Netty将再次调用该方法，最终将累计所有4个字节。
            if (buf.readableBytes() >= 4) { // (3)
                long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
                System.out.println(new Date(currentTimeMillis));
                ctx.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }


    /**
     * 虽然第一个解决方案已经解决了TIME客户端的问题，但修改后的处理程序看起来并不干净。
     * 想象一个更复杂的协议，它由多个字段组成，例如可变长度字段。您的ChannelInboundHandler实施将很快变得无法维护。
     * <p>
     * 您可能已经注意到，您可以ChannelHandler为a 添加多个ChannelPipeline，
     * 因此，您可以将一个单片拆分ChannelHandler为多个模块化，以降低应用程序的复杂性。
     * 例如，您可以拆分TimeClientHandler为两个处理程序：
     * <p>
     * TimeDecoder 它涉及碎片问题，以及
     * 最初的简单版本TimeClientHandler。
     * 幸运的是，Netty提供了一个可扩展的类，可以帮助您编写第一个开箱即用的类：
     */

//ByteToMessageDecoder是一种实现ChannelInboundHandler，可以很容易地处理碎片问题。
    class TimeDecoder extends ByteToMessageDecoder { // (1)
        @Override
        //ByteToMessageDecoderdecode()每当收到新数据时，都会使用内部维护的累积缓冲区调用该方法。
        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) { // (2)
            if (in.readableBytes() < 4) {
                //decode()可以决定out在累积缓冲区中没有足够数据的地方添加任何内容。
                // 收到更多数据后ByteToMessageDecoder会decode()再次拨打电话。
                return; // (3)
            }
            //如果decode()添加对象out，则意味着解码器成功解码了消息。
            // ByteToMessageDecoder将丢弃累积缓冲区的读取部分。
            // 请记住，您不需要解码多条消息。ByteToMessageDecoder将继续调用该decode()方法，直到它不添加任何内容out。
            out.add(in.readBytes(4)); // (4)
        }
        /**
         * 此外，Netty提供开箱即用的解码器，使您能够非常轻松地实现大多数协议，
         * 并帮助您避免最终导致单片不可维护的处理程序实现。有关更多详细示例，请参阅以下软件包：
         *
         * io.netty.example.factorial 对于二进制协议，和
         * io.netty.example.telnet 用于基于文本行的协议。
         * */
    }
}