package io.netty.POJOByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TimeDecoder extends ByteToMessageDecoder {

    // (1)
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
        out.add(new UnixTime(in.readUnsignedInt())); // (4)
    }
    /**
     * 此外，Netty提供开箱即用的解码器，使您能够非常轻松地实现大多数协议，
     * 并帮助您避免最终导致单片不可维护的处理程序实现。有关更多详细示例，请参阅以下软件包：
     *
     * io.netty.example.factorial 对于二进制协议，和
     * io.netty.example.telnet 用于基于文本行的协议。
     * */
}