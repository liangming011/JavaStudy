package io.netty.POJOByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;

public class TimeEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int)m.value());

        //这一行中有很多重要的事情。
        //
        //首先，我们按原样传递原始文件ChannelPromise，以便当编码数据实际写入线路时，Netty将其标记为成功或失败。
        //
        //第二，我们没有打电话ctx.flush()。有一个单独的处理程序方法void flush(ChannelHandlerContext ctx)，用于覆盖flush()操作。
        //
        //为了进一步简化，您可以使用MessageToByteEncoder：
        ctx.write(encoded, promise); // (1)
    }
}

class TimeEncoder0 extends MessageToByteEncoder<UnixTime> {
    @Override
    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) {
        out.writeInt((int)msg.value());
    }
}
