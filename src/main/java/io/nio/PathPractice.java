package io.nio;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathPractice {

    public static void main(String[] args) {

        //创建绝对路径
        Path path= Paths.get("/Users/liangming/Java/JavaStudy/data.txt");
        System.out.println("绝对路径 "+path);

        //创建相对路径
        Path projects = Paths.get("/Users/liangming/Java/", "JavaStudy");
        Path file     = Paths.get("/Users/liangming/Java/", "JavaStudy/data.txt");
        System.out.println("相对路径 "+projects);
        System.out.println("相对路径 "+file);

        //当前目录
        Path currentDir = Paths.get(".");
        System.out.println("当前目录 "+currentDir.toAbsolutePath());

        //当前目录父目录
        Path parentDir = Paths.get("..");
        System.out.println("当前目录父目录 "+parentDir.toAbsolutePath());


        /**
         * Paths.normalize()
         * */
        String originalPath ="/Users/liangming/Java/./JavaStudy";

        Path path1 = Paths.get(originalPath);
        System.out.println("path1 = " + path1);

        Path path2 = path1.normalize();
        System.out.println("path2 = " + path2);
    }
}
