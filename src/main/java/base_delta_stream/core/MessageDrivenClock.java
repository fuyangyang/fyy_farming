package base_delta_stream.core;


import base_delta_stream.core.compact.CompactionTask;
import base_delta_stream.core.metadata.MetaDataManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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

    public MessageDrivenClock(String modelPath) {
        this.modelPath = modelPath;
        metaDataManager = new MetaDataManager(modelPath);
        queue = new ArrayBlockingQueue(24);
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
//        queue.put(compactionTask);
        compactionTask.run();
    }

    @Override
    public void onMinute(Long nextMinute) throws Exception {
        streamFileHandler.onMinute(nextMinute);
    }

    @Override
    protected void handleElement(Message message) throws Exception {
        streamFileHandler.handleElement(message);
    }
}
