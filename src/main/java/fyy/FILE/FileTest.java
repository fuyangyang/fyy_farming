package fyy.FILE;

import java.io.File;

/**
 * Created by fuyi on 17/5/29.
 */
public class FileTest {
    public static void main(String[] args) {
        listFiles("/Users/fyy/test/file");
    }

    private static void listFiles(String name) {
        File file = new File(name);

//        if(file.isDirectory()) {
//            System.out.println("file:" + name);
//            File[] files = file.listFiles();
//            for(int i = 0; i < files.length; i++) {
//                listFiles(files[i].getAbsolutePath());
//            }
//        } else {
//            System.out.println("file:" + name);
//        }

//        System.out.println("********");
        if (file.isDirectory()) {
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                System.out.println((files[i]));
            }


            File[] fs = file.listFiles();
            for (int i = 0; i < fs.length; i++) {
                System.out.println((fs[i].getAbsoluteFile()));
                System.out.println((fs[i].getName()));
            }
        }
    }
}
