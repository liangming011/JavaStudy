package io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReadWrite {

    public static void main(String[] args) throws IOException {
        char c;
        // 使用 System.in 创建 BufferedReader
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("输入字符, 按下 'q' 键退出。");

        // 读取System.in字符串
        String d = br.readLine();
        System.out.println(d);

        // 读取System.in字符
        do {
            c = (char)br.read();
            System.out.println(c);
        } while (c != 'q');

        // 写入System.out字符
        int b;
        b = 'A';
        System.out.write(b);
        System.out.write('\n');

    }
}
