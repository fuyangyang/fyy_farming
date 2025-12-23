package guava.file;

import com.google.common.io.Files;

import java.io.File;

public class FileWriter {
    private static final int COUNT = 1500000;
    public static void main(String[] args) throws Exception {
        String fileName = "/Users/fuyangyang/github/fyy_farming/largeFile.txt";
        final File newFile = new File(fileName);
        StringBuilder sb = new StringBuilder(COUNT * 6);
        for (int i = 0; i < COUNT; i++) {
            sb.append(i + System.lineSeparator());
        }
        Files.write(sb.toString().getBytes(), newFile);
    }

}
