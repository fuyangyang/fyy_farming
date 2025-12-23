package thread.two_phase_termination;

import java.util.concurrent.TimeUnit;

/**
 * 未启动的线程不是New状态，这个线程都还没创建出来，jstack看不到。故捕捉到NEW状态还是挺困难的
 */
public class ThreadNewStateTest {

    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("finished");
            }
        }, "sleep_thread");

        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.start();
    }
}
