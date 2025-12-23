package nio;


import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


/**
 * https://blog.csdn.net/napolunyishi/article/details/18214929
 * https://blog.csdn.net/zhxdick/article/details/81130102
 * https://www.cnblogs.com/charlesblc/p/6263665.html
 * https://www.jianshu.com/p/1cb00b599079
 *
 *
 *
 * 进一步的理解：
 * （1）mmap需要预先指定大小，且所有操作必须限定在指定大小内，否则报BufferOverflowException
 * （2）mmap映射过程耗时还ok，只要操作系统去拿一块连续的磁盘空间
 * （3）mmap可以进程间共享，mmap使用的是堆外内存，不在JVM内，如果在JVM内就不能进程共享了
 * （4）mmap相比文件操作免去了用户内存和核心态内存的一次数据拷贝过程
 * （5）写一块数据到磁盘，用mmap和带缓冲的写磁盘(只能顺序写)两种方式，mmap整体性能高10倍, 如果磁盘写性能是瓶颈的话，应该差不多啊，mmap也要从内存写到磁盘。
 * （6）mmap与直接内存有什么关系？mmap目标是把数据写到磁盘，而直接内存（也叫堆外内存）是使用JVM堆内存之外的内存，防止Full GC。
 */
public class MemoryMappedFileTest {

    private static final int COUNT = 10;

    /**
     * 简单的从文件中读写数据
     * @throws Exception
     */
    public static void test() throws Exception {
        RandomAccessFile memoryMappedFile = new RandomAccessFile("/Users/fuyangyang/github/fyy_farming/largeFile.txt", "rw");
        
        // Mapping a file into memory
        MappedByteBuffer mmap = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, COUNT);

        // Writing into Memory Mapped File
        for (int i = 0; i < COUNT; i++) {
            mmap.put((byte) 'A');
        }
        mmap.force();//不force数据可能写不到storage
        System.out.println("Writing to Memory Mapped File is completed");

        // reading from memory file in Java
        for (int i = 0; i <= COUNT; i++) {
            System.out.print((char) mmap.get(i));
        }
        System.out.println("Reading from Memory Mapped File is completed");
        memoryMappedFile.close();
    }

    /**
     * mac SSD
     *
     * nio性能比bufferwrite快10倍, 但是用mmap put完的数据可能会丢失，而写磁盘不会丢
     * 下面写了100000000个A字节到文件中，容量约为95M，下面是性能数据
     * Writing to Memory Mapped File is completed, cost: 204 ms
     * Writing to normal File is completed, cost: 2197 ms
     *
     * 如果每次写4k，bos 100ms， mmap 156ms
     *
     * 写950M的数据，两者性能也还是差10倍
     * @throws Exception
     */
    public static void testPerformace() throws Exception {
        int size = 100000000;
        int chunkSize = 4096;
        int loop = size /chunkSize;
        byte[] content = new byte[chunkSize];
        for (int i = 0; i < chunkSize; i++) {
            content[i] = 49;
        }
        RandomAccessFile memoryMappedFile = new RandomAccessFile("/Users/fuyangyang/github/fyy_farming/mmap.txt", "rw");
        MappedByteBuffer mmap = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, size);
        long start = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            mmap.put(content);
        }
//        for (int i = 0; i < size; i++) {
//            mmap.put((byte) 'A');
//        }
        mmap.force();
        long stop = System.currentTimeMillis();
        System.out.println("Writing to Memory Mapped File is completed, cost: " + (stop - start) + " ms");
        memoryMappedFile.close();

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("/Users/fuyangyang/github/fyy_farming/not_mmap.txt")));
        long begin = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            bos.write(content);
        }
//        for (int i = 0; i < size; i++) {
//            bos.write((byte) 'A');
//        }
        bos.flush();
        bos.close();
        long end = System.currentTimeMillis();
        System.out.println("Writing to normal File is completed, cost: " + (end - begin) + " ms");
    }

    /**
     * 映射大小为10， 但put 21个，当操作到10个以上时报错
     * @throws Exception
     */
    public static void testAbnormalCase() throws Exception {
        int size = 10;
        RandomAccessFile memoryMappedFile = new RandomAccessFile("/Users/fuyangyang/github/fyy_farming/mmap_cas1.txt", "rw");
        MappedByteBuffer mmap = memoryMappedFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0, size);
        for (int i = 0; i < size + 11; i++) {
            mmap.put((byte) 'A');
            System.out.println(i);
        }
        mmap.force();
        memoryMappedFile.close();
    }

    public static void main(String[] args) throws Exception {
        testPerformace();
    }
}
