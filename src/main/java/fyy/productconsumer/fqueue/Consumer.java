package fyy.productconsumer.fqueue;

import com.github.cgdon.fqueue.FQueue;
import fyy.productconsumer.blockqueue.PCData;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private FQueue queue;
    private static final int SLEEPTIME = 322;

    public Consumer(FQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random r = new Random();
        int num = 0;
        System.out.println("start Consumer id :" + Thread.currentThread().getId());
        try {
            while (true) {
                byte[] data = queue.poll();
                if (data != null) {
//                    System.out.println(num++);
//                    if (num % 10 == 0) {
//                        System.exit(0);
//                    }
//                    System.out.println("consume " + new String(data));
                    System.out.println("consume ");
                } else {
                    System.out.println("data is null");
                }
                Thread.sleep(r.nextInt(SLEEPTIME));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}