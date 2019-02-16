package io;

import java.io.File;
import java.io.FilenameFilter;

public class ListFilesAndList implements FilenameFilter {

    /**
     * @param args
     */
    public static void main(String[] args) {
        new ListFilesAndList().f();
    }

    public void f(){
        File f=new File("/Users/liangming/Java/JavaStudy/src/main/java/io");
        String[] filename=f.list();//以字符串的形式，先列出当前目录下所有文件看一下（有什么类型的文件）
        for(int i=0;i<filename.length;i++){
            System.out.println(filename[i]);
        }
        System.out.println("********************************");
        String[] fname=f.list(this);
        for(int i=0;i<fname.length;i++){
            System.out.println(fname[i]);
        }
        System.out.println("********************************");
        File[] fnamel=f.listFiles(this);
        for(int i=0;i<fnamel.length;i++){
            System.out.println(fnamel[i]);
        }
    }

    @Override
    public boolean accept(File f, String name) {//重写接口方法
        //return f.isDirectory();
        return name.endsWith(".java");//返回当前目录下以.java结尾的文件
    }
}
