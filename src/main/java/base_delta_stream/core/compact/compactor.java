package base_delta_stream.core.compact;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 从queue中提取任务，然后提交给线程池执行
 */
public class compactor {

    private BlockingQueue<CompactionTask> queue;
    private ExecutorService executor;

    public compactor(BlockingQueue queue) {
        this.queue = queue;
        executor = new ThreadPoolExecutor(2, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue(1),
                new CompactionThreadFactory("compactor"));
    }

    public void run() {
        while (true) {
            CompactionTask compactionTask;
            try {
                compactionTask = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            executor.submit(compactionTask);
        }
    }

    private static class CompactionThreadFactory implements ThreadFactory {
        private final AtomicInteger threadIndex = new AtomicInteger(0);
        private final String threadName;

        public CompactionThreadFactory(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public Thread newThread(Runnable r) {
            String name = threadName + "-" + threadIndex.incrementAndGet();
//            LOG.info("One thread={} created", name);
            Thread thread = new Thread(r, name);
            thread.setDaemon(true);
            return thread;
        }

    }

}
