package fyy.productconsumer.fqueue;

import com.github.cgdon.fqueue.FQueue;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
//测试大点的数据
public class Producer implements Runnable {
    private volatile boolean isRunning = true;
    private FQueue queue;// 内存缓冲区
//    private static AtomicInteger count = new AtomicInteger();// 总数 原子操作
    private static final int SLEEPTIME = 3;
    private static String suffix;

    static {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 24024; i++) {
            sb.append(1);
        }
        System.out.println("sb.length:" + sb.toString().length());
        suffix = sb.toString();
    }

    public Producer(FQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random r = new Random();
        System.out.println("start producting id:" + Thread.currentThread().getId());
        try {
            int num = 0;
            while (isRunning) {
//                Thread.sleep(r.nextInt(SLEEPTIME));
//                int num = count.incrementAndGet();

                //重试: 一直重试，还是试N次
                if (queue.size() > 400) {
                    System.out.println("over 400 sleep 1s");
                    Thread.sleep(1000);
                    continue;
                }
                System.out.println(num++ + " 加入队列");
                boolean status = queue.offer(suffix.getBytes());
//                boolean status = queue.offer(null);
                if (! status) {
                    System.err.println(" 加入队列失败");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void stop() {
        isRunning = false;
    }

}