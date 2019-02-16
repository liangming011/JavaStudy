package io.netty.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 一个ChannelHandlerContext对象提供的各种操作，使您可以触发各种I/O的事件和操作。
        // 在这里，我们调用write(Object)逐字写入收到的消息。
        // 请注意，与DISCARD示例中的操作不同，我们没有发布收到的消息。这是因为Netty在写入线路时会为您发布。
        ctx.write(msg); // (1)

        //ctx.write(Object)不会将消息写入线上。
        // 它在内部进行缓冲，然后通过以下方式冲洗到线上ctx.flush()。
        // 或者，您可以要求ctx.writeAndFlush(msg)简洁。
        ctx.flush(); // (2)
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.flush();
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}