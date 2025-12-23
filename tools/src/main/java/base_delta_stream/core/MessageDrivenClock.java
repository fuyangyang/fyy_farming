package base_delta_stream.core;


import base_delta_stream.core.compact.CompactionTask;
import base_delta_stream.core.compact.Compactor;
import base_delta_stream.core.metadata.MetaDataManager;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 注意：
 * 保证每分钟的stream文件生成，然后再看下是否触发合并任务，合并任务包括两种：1）只合并delta；2）合并delta后合并base。
 * 如果触发，则把合并任务提交到合并队列。
 * 然后接着接收消息，生成下一个stream文件。
 */
public class MessageDrivenClock extends AbstractClock<Message> {

    private StreamFileHandler streamFileHandler;
    private BlockingQueue<CompactionTask> queue;
    private String modelPath;
    private MetaDataManager metaDataManager;
    private ExecutorService executor;

    public MessageDrivenClock(String modelPath) {
        this.modelPath = modelPath;
        metaDataManager = new MetaDataManager(modelPath);
        queue = new ArrayBlockingQueue(24);

        executor = new ThreadPoolExecutor(2, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue(1),
                new CompactionThreadFactory("Compactor"));

        executor.submit(new Compactor(queue));
        executor.submit(new Compactor(queue));
    }

    @Override
    protected void initial() {
        streamFileHandler = new StreamFileHandler(modelPath, getCurrentMinute(), metaDataManager);
    }

    @Override
    public long extractTimestamp(Message message) {
        return message.getTimestamp();
    }

    @Override
    public void onHour(boolean reachNextDay, Long nextDay) throws Exception {
        CompactionTask compactionTask = new CompactionTask(getCurrentHour(), modelPath, reachNextDay,
                streamFileHandler.fetchStreamListToCompact(), null, metaDataManager);
        queue.put(compactionTask);
    }

    @Override
    public void onMinute(Long nextMinute) throws Exception {
        streamFileHandler.onMinute(nextMinute);
    }

    @Override
    protected void handleElement(Message message) throws Exception {
        streamFileHandler.handleElement(message);
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
            System.out.println(name + " created");
            Thread thread = new Thread(r, name);
            thread.setDaemon(true);
            return thread;
        }

    }
}
