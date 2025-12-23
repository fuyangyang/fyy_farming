package FILE;

import com.sun.management.OperatingSystemMXBean;

import java.io.File;
import java.lang.management.ManagementFactory;

/**
 * Created by fuyi on 17/5/29.
 */
public class FileTest {
    public static void main(String[] args) {
//        listFiles("/Users/fyy/test/file");
//        getDiskCapacity();
        getMemCapacity();
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

    /**
     * 获取硬盘使用情况
     */
    private static void getDiskCapacity() {
        File[] roots = File.listRoots();
        for (File _file : roots) {
            System.out.println(_file.getPath());
            System.out.println("Free space = " + (_file.getFreeSpace() / (1024 * 1024)) / 1024 + "G");
            System.out.println("Usable space = " + _file.getUsableSpace());
            System.out.println("Total space = " + _file.getTotalSpace());
            System.out.println("used space  = " + (_file.getTotalSpace() - _file.getFreeSpace()));
            System.out.println();
        }
    }

    /**
     * 获取内存使用情况
     */
    private static void getMemCapacity() {
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println("系统物理内存总计：" + osmb.getTotalPhysicalMemorySize() / 1024/1024 + "MB");
        System.out.println("系统物理可用内存总计：" + osmb.getFreePhysicalMemorySize() / 1024/1024 + "MB");
    }
}
