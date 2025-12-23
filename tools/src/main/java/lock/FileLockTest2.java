package lock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Date;

public class FileLockTest2 {
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
