package base_delta_stream;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;

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
}
