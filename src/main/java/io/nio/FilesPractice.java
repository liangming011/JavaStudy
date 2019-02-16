package io.nio;

import java.io.IOException;
import java.nio.file.*;

public class FilesPractice {

    public static void main(String[] args) {

        //Files.exists（）方法检查文件系统中是否存在给定的Path。
        Path path = Paths.get("./data.txt");
        boolean pathExists =Files.exists(path,new LinkOption[]{ LinkOption.NOFOLLOW_LINKS});
        System.out.println(pathExists);

        //Files.createDirectory()创建文件夹
        Path path1 = Paths.get("./subdir");
        try {
            Path newDir = Files.createDirectory(path1);
        } catch(FileAlreadyExistsException e){
            // the directory already exists.
        } catch (IOException e) {
            //something else went wrong
            e.printStackTrace();
        }

        //Files.copy()方法将文件从一个路径复制到另一个路径。这是一个Java NIO Files.copy（）示例：
        Path sourcePath      = Paths.get("./data.txt");
        Path destinationPath = Paths.get("./subdir/data-copy.properties");
        try {
            Files.copy(sourcePath, destinationPath);
        } catch(FileAlreadyExistsException e) {
            //destination file already exists
        } catch (IOException e) {
            //something else went wrong
            e.printStackTrace();
        }

        //覆盖现有的文件
        Path sourcePath1      = Paths.get("./data.txt");
        Path destinationPath1 = Paths.get("./subdir/data-copy.properties");
        try {
            Files.copy(sourcePath1, destinationPath1,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch(FileAlreadyExistsException e) {
            //destination file already exists
        } catch (IOException e) {
            //something else went wrong
            e.printStackTrace();
        }

        //Files.move() Java NIO Files类还包含用于将文件从一个路径移动到另一个路径的功能。
        //renameTo() 重命名
        Path sourcePath2      = Paths.get("./data.txt");
        Path destinationPath2 = Paths.get("./subdir/data-copy.properties");

        try {
            Files.move(sourcePath2, destinationPath2,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            //moving file failed.
            e.printStackTrace();
        }

        //Files.delete():方法可以删除文件或空目录
        Path path2 = Paths.get("./subdir");

        try {
            Files.delete(path2);
        } catch (IOException e) {
            //deleting file failed
            e.printStackTrace();
        }

        //Files.walkFileTree():方法包含用于递归遍历目录树的功能,方法将Path实例和FileVisitor作为参数。
        // Path实例指向要遍历的目录。在traversion期间调用FileVisitor。
    }
}
