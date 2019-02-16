package io.netty.POJOByteBuf;

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
        ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        System.out.println(new UnixTime().toString());
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
