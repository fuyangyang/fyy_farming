package base_delta_stream.utils;

import java.io.*;

public class FileUtils {
    public static void closeFile(Closeable closeable) throws Exception {
        closeable.close();
    }

    public static BufferedWriter getFileWriter(String filePath) throws Exception {
        File file = new File(filePath);
        String parent = file.getParent();
        File parentFile = new File(parent);
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
        return new BufferedWriter(new FileWriter(file));
    }

    public static BufferedWriter getFileWriter(String prefix, String fileName) throws IOException {
        File file = new File(prefix, fileName);
        String parent = file.getParent();
        File parentFile = new File(parent);
        if (!parentFile.exists()) {
            parentFile.mkdir();
        }
        return new BufferedWriter(new FileWriter(file));
    }
}
