package thread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 本示例说明当线程池中的线程挂掉后,会启新线程补充
 * Created by fuyi on 17/1/19.
 */
public class ThreadPoolTest {

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(Integer.valueOf(10));

        for(int i=0; i < 1000; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    boolean crush = new Random().nextBoolean();
                    if (crush) {
                        throw new RuntimeException("crushed");
                    } else {
                        System.out.println("finished");
                    }
                }
            });
        }

        threadPool.shutdown();
        try {
            threadPool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (Exception e) {

        }

        System.out.println("over");
    }
}
