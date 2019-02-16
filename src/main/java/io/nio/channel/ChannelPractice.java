package io.nio.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * http://ifeve.com/channels/
 *
 * Java NIO的通道类似流，但又有些不同：
 *
 * 1.既可以从通道中读取数据，又可以写数据到通道。但流的读写通常是单向的。
 * 2.通道可以异步地读写。
 * 3.通道中的数据总是要先读到一个Buffer，或者总是要从一个Buffer中写入。
 *
 * Channel的实现
 * 这些是Java NIO中最重要的通道的实现：
 *
 * FileChannel  ： 从文件中读写数据。
 * DatagramChannel ：能通过UDP读写网络中的数据。
 * SocketChannel ：能通过TCP读写网络中的数据。
 * ServerSocketChannel ：可以监听新进来的TCP连接，像Web服务器那样。对每一个新进来的连接都会创建一个SocketChannel
 *
 * */
public class ChannelPractice {

    //基本的 Channel 示例
    public static void main(String[] args) {
        try {
            RandomAccessFile aFile = new RandomAccessFile("pom.xml", "rw");

            FileChannel inChannel = aFile.getChannel();

            ByteBuffer buf = ByteBuffer.allocate(48);
            int bytesRead = inChannel.read(buf);
            while (bytesRead != -1) {

                System.out.println("Read " + bytesRead);
                //因为是先进先出，需要翻转。从写模式切换到读模式
                buf.flip();

                while (buf.hasRemaining()) {
                    System.out.print((char) buf.get());
                }

                buf.clear();
                bytesRead = inChannel.read(buf);
            }
            aFile.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

}
