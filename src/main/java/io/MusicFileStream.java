package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 自己写了一个简单的小程序用来剪辑特定长度的音频，并将它们混剪在一起，大体思路是这样的：
 *
 * 1. 使用 FileInputStream 输入两个音频
 *
 * 2. 使用 FileInputStream的skip(long n) 方法跳过特定字节长度的音频文件，比如说：输入 skip(1024*1024*3)，这样就能丢弃掉音频文件前面的 3MB 的内容。
 *
 * 3. 截取中间特定长度的音频文件：每次输入 8KB 的内容，使用 count 记录输入次数，达到设置的次数就终止音频输入。比如说要截取 2MB 的音频，每次往输入流中输入 8KB 的内容，就要输入 1024*2/8 次。
 *
 * 4. 往同一个输出流 FileOutputStream 中输出音频，并生成文件，实现音频混合。
 * */
public class MusicFileStream {

    public static void main(String[] args){

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;

        String[] filenames = {"/Users/liangming/Java/JavaStudy/src/main/java/io/我只在乎你.mp3","/Users/liangming/Java/JavaStudy/src/main/java/io/星月神话.mp3"};

        //设置byte数组，每次往输出流中传入8K的内容
        byte by[] = new byte[1024*8];

        try {
            //声明要合并的文件
            fileOutputStream = new FileOutputStream("/Users/liangming/Java/JavaStudy/src/main/java/io/合并.mp3");

            for (int i = 0; i < filenames.length; i++) {

                //读取文件
                fileInputStream = new FileInputStream(filenames[i]);

                //跳过开头3M
                fileInputStream.skip(1024 * 1024 * 3);

                //确保还有8k 数据
                int count = 0;
                while (fileInputStream.read(by) != -1) {
                    fileOutputStream.write(by);

                    count++;
                    //设置仅读取中间 2M 内容
                    if (count * 8 * 1024 == 1024 * 1024 * 2) {
                        break;
                    }
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
                fileOutputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
