package base_delta_stream.core.compact;

import java.util.concurrent.*;

/**
 * 从queue中提取任务，然后提交给线程池执行
 */
public class Compactor implements Runnable{

    private BlockingQueue<CompactionTask> queue;


    public Compactor(BlockingQueue queue) {
        this.queue = queue;

    }

    @Override
    public void run() {
        System.out.println("compactor starts to run...");
        while (true) {
            CompactionTask compactionTask;
            try {
                compactionTask = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            compactionTask.compact();
        }
    }
}
