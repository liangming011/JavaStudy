package io.netty.discard;
import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;


/**
 * 处理服务器端通道。
 */
/**
 * 1. DiscardServerHandlerextends ChannelInboundHandlerAdapter，这是一个实现ChannelInboundHandler。
 *    ChannelInboundHandler提供可以覆盖的各种事件处理程序方法。
 *    目前，只需自己扩展ChannelInboundHandlerAdapter而不是实现处理程序接口。
 **/
public class DiscardServerHandler extends ChannelInboundHandlerAdapter { // (1)

    /**
     * 2. 我们在channelRead()这里覆盖事件处理程序方法。
     *    每当从客户端接收到新数据时，都会使用收到的消息调用此方法。在此示例中，接收消息的类型是ByteBuf。
     **/
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        /**
         * 3. 要实现该DISCARD协议，处理程序必须忽略收到的消息。
         *    ByteBuf是一个引用计数对象，必须通过该release()方法显式释放。
         *    请记住，处理程序有责任释放传递给处理程序的任何引用计数对象。通常，channelRead()handler方法实现如下：
         **/
        // 静默丢弃收到的数据。
        //((ByteBuf) msg).release(); // (3)
        ByteBuf in = (ByteBuf) msg;
        try {
            //4. 个效率低下的循环实际上可以简化为： System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII))
            while (in.isReadable()) {
                System.out.print((char) in.readByte()); // (4)
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg); // (5):或者，你可以在in.release()这里做。
        }
    }

    /**
     * 4. 该exceptionCaught()事件处理方法被调用，可抛出异常时被提出的Netty由于I / O错误或由处理器实现，
     *    由于在处理事件引发的异常。在大多数情况下，应记录捕获的异常并在此处关闭其关联的通道，尽管此方法的
     *    实现可能会有所不同，具体取决于您要处理特殊情况的操作。例如，您可能希望在关闭连接之前发送带有错误代码的响应消息。
     **/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // 引发异常时关闭连接。
        cause.printStackTrace();
        ctx.close();
    }


}