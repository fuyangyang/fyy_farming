package productconsumer.fqueue;

import com.github.cgdon.fqueue.FQueue;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * https://www.cnblogs.com/chentingk/p/6497107.html
 * 正式用的时候加生命周期
 */
public class Main {
    private static String suffix;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24024; i++) {
            sb.append(1);
        }
        System.out.println("sb.length:" + sb.toString().length());
        suffix = sb.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("start at " + new Date());
        long start = System.currentTimeMillis();
        String path = "/Users/fuyangyang/github/fyy_farming/src/main/java/fyy/productconsumer/fqueue/db";
        FQueue queue = new FQueue(path, 1 * 1024 * 1024);
        System.out.println("size:" + queue.size());
//        queue.clear();
//        System.out.println("clear");
//        System.out.println("size:" + queue.size());

//        for (int i = 0; i < 100; i++) {
//            Thread.sleep(10);
//            queue.offer(String.valueOf(suffix).getBytes());
//            System.out.println("offer " + i);
//        }
//
//        queue.close();
//        System.out.println("si:" + queue.size());
//        int i = 0;
//        while (true) {
//            byte[] poll = queue.poll();
//            Thread.sleep(500);
//            if(poll != null) {
//                System.out.println(i++);
//            } else {
//                System.out.println("null");
//                System.exit(0);
//            }
//        }


        ExecutorService service = Executors.newCachedThreadPool();
        int produceCount = 4;
        int cousumerCount = produceCount;
        for (int i = 0; i < produceCount; i++) {
            service.execute(new Producer(queue));
        }

        for (int i = 0; i < cousumerCount; i++) {
            service.execute(new Consumer(queue));
        }
//        p1.stop();
//        p2.stop();
//        p3.stop();
//        Thread.sleep(3000);
//        service.shutdown();

    }
}
