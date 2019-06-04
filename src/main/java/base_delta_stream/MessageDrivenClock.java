package base_delta_stream;

import java.io.IOException;

public class MessageDrivenClock extends AbstractClock<Message> {


    private StreamFileHandler streamFileHandler;
    private MetaDataManager metaDataManager;
    private String modelPath;

    public MessageDrivenClock(String modelPath) {
        this.modelPath = modelPath;
        init();
    }

    public void init() {
        streamFileHandler = new StreamFileHandler(modelPath);
        metaDataManager = new MetaDataManager(modelPath);
    }

    @Override
    public long extractTimestamp(Message message) {
        return message.getTimestamp();
    }

    @Override
    public void onHour(boolean reachNextDay) {
        //小时级处理器
    }

    @Override
    public void onMinute() {
        //分钟文件处理器
        streamFileHandler.onMinute();
    }

    //TODO message和时间要不要封装成一个DTO类
    @Override
    protected void handleElement(Message message) throws Exception {
        streamFileHandler.handleElement(message, getCurrentMinute());
    }
}
