package nio;

import java.nio.ByteBuffer;

/**
 * Created by chris on 18/4/9.
 */
public class DirectIOTest {
    public static void main(String[] args) {
        useDirectMemory();
        useHeapMemory();
    }

    private static void useDirectMemory() {
        long start = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocateDirect(500);//分配500个字节的DirectBuffer
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 99; j++) {
                buffer.putInt(j);           //向DirectBuffer写入数据
            }
            buffer.flip();
            for (int j = 0; j < 99; j++) {
                buffer.get();                   //从DirectBuffer中读取数据
            }
            buffer.clear();
        }
        System.out.println("DirectBuffer use : " + (System.currentTimeMillis() - start) + "ms");
    }

    private static void useHeapMemory() {
        long start = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(500);//分配500个字节的ByteBuffer
        for (int i = 0;i < 100000; i++) {
            for (int j = 0; j < 99; j++) {
                buffer.putInt(j);           //向DirectBuffer写入数据
            }
            buffer.flip();
            for (int j = 0; j < 99; j++) {
                buffer.get();                   //从DirectBuffer中读取数据
            }
            buffer.clear();
        }

        System.out.println("ByteBuffer use : " + (System.currentTimeMillis() - start) + "ms");
    }

}
