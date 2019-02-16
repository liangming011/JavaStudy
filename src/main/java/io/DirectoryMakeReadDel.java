package io;

import java.io.File;

public class DirectoryMakeReadDel {

    public static void main(String args[]) {

        // 现在创建目录
//        String dirname1 = "tmps/user/java/bin";
//        makedir(dirname1);

        // 现在读取目录
//        String dirname2 = "tmps";
//        readdir(dirname2);

        // 删除文件及目录
        File folder = new File("tmps");
        deleteFolder(folder);
    }



    /**
     *  现在创建目录
     * */
    public static void makedir(String dirname) {

        File d = new File(dirname);
        // 现在创建目录

        //mkdir( )方法创建一个文件夹，成功则返回true，失败则返回false。
        // 失败表明File对象指定的路径已经存在，或者由于整个路径还不存在，该文件夹不能被创建。
        boolean flag = d.mkdir();

        //mkdirs()方法创建一个文件夹和它的所有父文件夹。
        boolean flags = d.mkdirs();

        System.out.println(flag);

        System.out.println(flags);
    }

    /**
     *  现在读取目录
     * */
    public static void readdir(String dirname) {

        File f1 = new File(dirname);
        if (f1.isDirectory()) {
            System.out.println("目录 " + dirname);
            String s[] = f1.list();
            for (int i = 0; i < s.length; i++) {
                File f = new File(dirname + "/" + s[i]);
                if (f.isDirectory()) {
                    System.out.println(s[i] + " 是一个目录");
                } else {
                    System.out.println(s[i] + " 是一个文件");
                }
            }
        } else {
            System.out.println(dirname + " 不是一个目录");
        }
    }

    /**
     *  现在删除目录或文件
     * */
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

}
