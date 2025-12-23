package nio;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * https://www.cnblogs.com/kuoAT/p/7010056.html
 * https://www.cnblogs.com/duanxz/p/4705164.html
 * https://blog.csdn.net/qq_26222859/article/details/80885757
 */
public class FileChannelTest {
    public static void main(String[] args) throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("/tmp/data/nio-data.txt", "rw");
        FileChannel inChannel = aFile.getChannel();
        //普通的读写
        //inChannel.read();
//        inChannel.write();

        //zero copy：不经过用户空间，少两次拷贝
//        inChannel.transferFrom();
//        inChannel.transferTo();

    }
}
