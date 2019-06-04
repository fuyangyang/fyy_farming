package base_delta_stream.core;

import base_delta_stream.core.Message;
import base_delta_stream.utils.FileUtils;
import base_delta_stream.utils.OffsetPair;
import utils.DateUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

/**
 * 管理分钟级文件的读写
 *
 * 负责：
 * 接受mafka消息的推送
 * 负责从消息中提取时间并及时更新时钟
 * 把消息写入stream文件，在写完一个文件后更新manifest文件。更新逻辑为：在manifest的stream列表头部插入一条entry，并置状态为假。具体做法：先生成manifest.tmp，然后rename成manifest。
 * <p>
 * 注意：
 * 保证每分钟的stream文件生成，然后再看下是否触发合并任务，合并任务包括两种：1）只合并delta；2）合并delta后合并base。
 * 如果触发，则把合并任务提交到合并队列。
 * 然后接着接收消息，生成下一个stream文件。
 */
public class StreamFileHandler {

    private OffsetPair offsetPair;

    /**
     * /path/to/model/stream/
     */
    private String filePathPrefix;

    /**
     * 文件句柄
     */
    private BufferedWriter streamWriter;

    /**
     * 当前文件名
     */
    private String currentFileName;

    public StreamFileHandler(String modelPath) {
        this.filePathPrefix = modelPath + "stream/";
        offsetPair = new OffsetPair();
    }

    public void handleElement(Message message, Long currentMinute) throws IOException {
        if (streamWriter == null) {
            generateFileName(currentMinute);
            streamWriter = FileUtils.getFileWriter(filePathPrefix, currentFileName);
        }
        long offset = message.getOffset();
        offsetPair.setOffset(offset);
        streamWriter.write(message.getContent() + ":" + DateUtils.getTime(message.getTimestamp()));
        streamWriter.newLine();
    }

    /**
     * 生成新文件的名称，时间改为下一分钟，
     */
    private void generateFileName(Long currentMinute) {
        currentFileName = DateUtils.getTime(currentMinute, "yyyy-MM-dd-HH-mm-ss") + ".writing";
    }

    /**
     * 关闭文件，在文件名上加起止offset，清空offset缓存
     */
    public void onMinute() {
        try {
            streamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        streamWriter = null;
        String finalFileName = currentFileName.split("\\.")[0] + "_" + offsetPair.getStart() + "_" + offsetPair.getEnd();
        new File(filePathPrefix + currentFileName).renameTo(new File(filePathPrefix + finalFileName));
        offsetPair.clear();
    }
}
