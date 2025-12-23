package lock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;

/**
 * 参考：https://www.cnblogs.com/XL-Liang/articles/2852998.html
 * 0.可以通过lock方法来获取文件锁，可以通过tryLock方来测试该文件锁是否可用。二者的区别是那：lock()方法是阻塞的方法，当文件锁不可用时，当前进程会被挂起；tryLock是非阻塞的方法，当文件锁不可用时，tryLock()会得到null值。
 *
 * 1.同一进程内，在文件锁没有被释放之前，不可以再次获取。即在release()方法调用前,只能lock()或者tryLock()一次。文件锁释放的几种方法：release方法，jvm退出，可以参考FileLock的注释
 *
 * 2. 进程锁对于同一进程来说是共享(shared)的，即这个进程中的线程都可以操作这个文件锁（且线程安全）；对于不同的进程来说是互斥(exclusive)的，因为FileLock保证只能有一个进程通过lock()或者tryLock()方法获得文件锁。
 *
 * 3. 文件锁的效果是与操作系统相关的。一些系统中文件锁是强制性的（mandatory），就当Java的某进程获得文件锁后，操作系统将保证其它进程无法对 文件做操作了。而另一些操作系统的文件锁是询问式的(advisory)，意思是说要想拥有进程互斥的效果，其它的进程也必须也按照API所规定的那样来 申请或者检测文件锁，不然，将起不到进程互斥的功能。所以，文档里建议将所有系统都当做是询问式系统来处理，这样程序更加安全也更容易移植。
 *
 * 测试：本类和FileLockTest2可以在idea中一起启动打断点测试
 */
public class FileLockTest {
    public static void main(String[] args){

        //文件锁所在文件
        File lockFile = new File("filelock");
        FileOutputStream outStream = null;
        FileLock lock = null;

        try {
            outStream = new FileOutputStream(lockFile);
            FileChannel channel = outStream.getChannel();
            try {
                //方法一
                lock = channel.lock();
                System.out.println("Get the Lock!");
                //do something...

                //方法二
                //lock = channel.tryLock();
                //if(lock != null){
                //  do something..
                //}

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally{
            if(null != lock){
                try {
                    System.out.println("Release The Lock"  + new Date().toString());
                    lock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outStream != null){
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
