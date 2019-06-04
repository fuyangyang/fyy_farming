package base_delta_stream.core;

import base_delta_stream.core.metadata.MetaDataManager;
import base_delta_stream.utils.FileUtils;
import base_delta_stream.utils.OffsetPair;
import com.google.common.collect.Lists;
import utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理分钟级文件的读写
 *
 * 负责接受mafka消息的推送
 * 负责把消息按N分钟写入stream文件，在写完一个文件后更新manifest文件。更新逻辑为：在manifest的stream列表头部插入一条entry，并置状态为假。具体做法：先生成manifest.tmp，然后rename成manifest。
 *
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
     * 正在写的文件名
     */
    private String writingFileName;

    private MetaDataManager metaDataManager;

    /**
     * 存放delta周期内的stream文件路径
     */
    private List<String> streamList = new ArrayList<>();

    public StreamFileHandler(String modelPath, Long currentMinute, MetaDataManager metaDataManager) {
        this.metaDataManager = metaDataManager;
        this.filePathPrefix = modelPath + "stream/";
        offsetPair = new OffsetPair();
        generateFileName(currentMinute);
        try {
            streamWriter = FileUtils.getFileWriter(filePathPrefix, writingFileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void handleElement(Message message) throws IOException {
        long offset = message.getOffset();
        offsetPair.setOffset(offset);
        streamWriter.write(message.getContent() + ":" + DateUtils.getTime(message.getTimestamp()));
        streamWriter.newLine();
    }

    /**
     * 生成新文件的名称，时间改为下一分钟，
     */
    private void generateFileName(Long currentMinute) {
        writingFileName = DateUtils.getTime(currentMinute, "yyyy-MM-dd-HH-mm-ss") + ".writing";
    }

    /**
     * 关闭文件，在文件名上加起止offset，清空offset缓存
     */
    public void onMinute(Long nextMinute) throws IOException {
        streamWriter.close();
        String streamFilePath = filePathPrefix + writingFileName.split("\\.")[0] + "_" + offsetPair.getStart() + "_" + offsetPair.getEnd();;
        AppendOffsetToFileName(streamFilePath);
        streamList.add(streamFilePath);
        metaDataManager.addStreamAndDump(streamFilePath);
        offsetPair.clear();
        generateFileName(nextMinute);
        streamWriter = FileUtils.getFileWriter(filePathPrefix, writingFileName);
    }

    private boolean AppendOffsetToFileName(String finalFilePath) {
        return FileUtils.renameTo(filePathPrefix + writingFileName, finalFilePath);
    }

    public List<String> fetchStreamListToCompact() {
        //TODO check deep copy
        ArrayList<String> list = Lists.newArrayList(streamList);
        streamList.clear();
        return list;
    }

}
