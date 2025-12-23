package thread;

import java.util.concurrent.TimeUnit;

public class SleepTest {


    /**
     * sleep中如果中断的话，会中断sleep，收到异常
     */
    static Thread sleepThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("Caught Exception: ");
                for (int i = 0; i < 10000000; i++) {
                    String x = "dsaf" + i;

                }
                e.printStackTrace();
            }
        }
    });

    /**
     * 即使interrupted，不处理的话任务也会继续跑，不会抛出中断异常
     */
    static Thread workThread = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("work thread starts");
            int i = 0;
            for (; i < 100000000; i++) {
                if(Thread.interrupted()) {
                    System.out.println("work thread interrupted");
                }
                String x = "dsaf" + i;
                x = "0";
            }
            System.out.println("i is: " + i);
            System.out.println("work thread finished!");
        }
    });

    public static void main(String[] args) throws InterruptedException {
//        sleepThread.start();
//        TimeUnit.SECONDS.sleep(1);
//        System.out.println("isInterrupted: " + sleepThread.isInterrupted());
//        sleepThread.interrupt();
//        System.out.println("isInterrupted: " + sleepThread.isInterrupted());

        workThread.start();
        workThread.interrupt();
    }
}
