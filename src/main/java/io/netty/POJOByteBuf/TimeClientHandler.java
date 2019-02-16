package io.netty.POJOByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * 如您所见，它与服务器端代码没有什么不同。
 * ChannelHandler实施怎么样？它应该从服务器接收一个32位整数，将其转换为人类可读的格式，打印翻译的时间，并关闭连接：
 * */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //在TCP / IP中，Netty将从对等体发送的数据读入ByteBuf。
        UnixTime m = (UnixTime) msg;
        System.out.println(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
