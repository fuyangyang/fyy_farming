package thread.two_phase_termination;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * t1拿锁不放，t2因拿锁阻塞了，阻塞时main线程interrupt t2，t2没有反应直到拿到锁
 */
public class InterruptWaitThread {
    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                try {
                    System.out.println("t1 sleeping...");
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("t1 release lock after sleeping");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("t2 is interrupted: " + Thread.currentThread().isInterrupted());
                lock.unlock();
            }
        });

        t1.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main thread interrupt t2");
        t2.interrupt();
    }
}
