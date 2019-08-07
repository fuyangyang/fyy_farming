package utils;

import java.io.File;

public class FileUtils {

    /**
     * 删除一个目录及里面的内容
     *
     * 递归删除，小心栈溢出
     * @param file
     * @return
     */
    public static boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }
}
