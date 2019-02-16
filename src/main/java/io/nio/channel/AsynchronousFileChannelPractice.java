package io.nio.channel;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

//http://tutorials.jenkov.com/java-nio/asynchronousfilechannel.html
public class AsynchronousFileChannelPractice {

    public static void main(String[] args) {
        Path path= Paths.get("./data.txt");

        try {
            //打开AsynchronousFileChannel
            AsynchronousFileChannel fileChannel =
                    AsynchronousFileChannel.open(path, StandardOpenOption.READ);

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            long position = 0;

            /**
             * 1. read()读数据
             * 2. 从AsynchronousFileChannel读取数据的第二种方法是调用以CompletionHandler作为参数的read（）方法版本。
             * */
            // read()数据
                Future<Integer> operation = fileChannel.read(buffer,position);

                while(!operation.isDone());

                buffer.flip();
                byte[] data = new byte[buffer.limit()];
                buffer.get(data);
                System.out.println(new String(data));
                buffer.clear();

            //从AsynchronousFileChannel读取数据的第二种方法是调用以CompletionHandler作为参数的read（）方法版本。
            fileChannel.read(buffer, position, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println("result = " + result);

                    attachment.flip();
                    byte[] data = new byte[attachment.limit()];
                    attachment.get(data);
                    System.out.println(new String(data));
                    attachment.clear();
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {

                }
            });

            /**
             * 1. write()写数据
             * 2. 从AsynchronousFileChannel读取数据的第二种方法是调用以CompletionHandler作为参数的write（）方法版本。
             * */

            // write()写数据
                Path path0 = Paths.get("data/test-write.txt");
                AsynchronousFileChannel fileChannel0 =
                        AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);

                ByteBuffer buffer0 = ByteBuffer.allocate(1024);
                long position0 = 0;

                buffer.put("test data".getBytes());
                buffer.flip();

                Future<Integer> operation0 = fileChannel.write(buffer0, position0);
                buffer.clear();

                while(!operation0.isDone());

                System.out.println("Write done");

            //从AsynchronousFileChannel读取数据的第二种方法是调用以CompletionHandler作为参数的write（）方法版本。

            if(!Files.exists(path0)){
                Files.createFile(path0);
            }

            fileChannel.write(buffer0, position0, buffer0, new CompletionHandler<Integer, ByteBuffer>() {

                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println("bytes written: " + result);
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.out.println("Write failed");
                    exc.printStackTrace();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
