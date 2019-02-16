package io.netty.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 实现的TIME协议是协议。
 * 它与前面的示例的不同之处在于，它发送包含32位整数的消息，而不接收任何请求，并在发送消息后关闭连接。
 * 在此示例中，您将学习如何构造和发送消息，以及在完成时关闭连接。
 *
 * 因为我们将忽略任何接收的数据，但是一旦建立连接就发送消息，channelRead()这次我们不能使用该方法。
 * 相反，我们应该覆盖该channelActive()方法。以下是实施：
 * */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    //如上所述，channelActive()当建立连接并准备生成流量时，将调用该方法。让我们写一个32位整数来表示这个方法中的当前时间。
    public void channelActive(final ChannelHandlerContext ctx) { // (1)

        // 要发送新消息，我们需要分配一个包含消息的新缓冲区。我们要写一个32位整数，
        // 因此我们需要一个ByteBuf容量至少为4个字节的数据。获取当前ByteBufAllocator通道ChannelHandlerContext.alloc()并分配新缓冲区。
        final ByteBuf time = ctx.alloc().buffer(4); // (2)
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        System.out.println(System.currentTimeMillis() / 1000L + 2208988800L);
        //像往常一样，我们编写构造的消息。
        /**
         * 但等等，翻转的地方在哪里？
         * java.nio.ByteBuffer.flip()在NIO中发送消息之前我们不习惯打电话吗？ByteBuf没有这样的方法，
         * 因为它有两个指针; 一个用于读操作，另一个用于写操作。当您ByteBuf在读取器索引未发生变化时写入内容时，写入器索引会增加。
         * reader索引和writer索引分别表示消息的开始和结束位置。
         *
         * 相比之下，NIO缓冲区没有提供一种干净的方法来确定消息内容的开始和结束位置，而无需调用flip方法。
         * 当您忘记翻转缓冲区时，您将遇到麻烦，因为不会发送任何数据或不正确的数据。
         * 在Netty中不会发生这样的错误，因为我们对不同的操作类型有不同的指针。
         * 你会发现它让你的生活变得更加轻松，因为你已经习惯了 - 没有翻身的生活！
         *
         * 另一点需要注意的是ChannelHandlerContext.write()（和writeAndFlush()）方法返回一个ChannelFuture。
         * A ChannelFuture表示尚未发生的I/O操作。
         * 这意味着，任何请求的操作可能尚未执行，因为所有操作在Netty中都是异步的。例如，以下代码可能会在发送消息之前关闭连接：
         *
         * Channel ch = ...;
         * ch.writeAndFlush(message);
         * ch.close();
         *
         * 因此，您需要close()在ChannelFuture完成后调用该方法，该write()方法由方法返回，并在写入操作完成时通知其侦听器。
         * 请注意，close()也可能不会立即关闭连接，并返回一个ChannelFuture。
         * */
        final ChannelFuture f = ctx.writeAndFlush(time); // (3)
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        }); // (4)
        /**
         * 当写请求完成后我们如何得到通知？
         * 这就像向ChannelFutureListener返回的内容添加一样简单ChannelFuture。
         * 在这里，我们创建了一个新的匿名函数ChannelFutureListener，它关闭Channel了操作完成的时间。
         *
         * 或者，您可以使用预定义的侦听器简化代码：
         * f.addListener(ChannelFutureListener.CLOSE);
         * */
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
