package base_delta_stream.core.compact;

import base_delta_stream.core.FileState;
import base_delta_stream.core.AbstractClock;
import base_delta_stream.core.metadata.MetaDataManager;

import java.io.IOException;
import java.util.List;

/**
 * 合并任务输出：
 *  * 1）合并成功后，把manifest中的被合并的文件状态置为true，更新metadata。然后把list提交到清除队列，让fc线程清除文件。
 */
public class CompactionTask implements Runnable {

    /**
     * 当前时间
     */
    private AbstractClock clock;

    /**
     * 是否触发base合并，如果触发，必须先合并完delta后再合并base。
     */
    private Boolean toCompackBase;

    /**
     * 待合并到delta的stream list
     */
    private List<FileState> streamList;

    /**
     * 待合并到base的delta list，如果不触发base合并，本参数可为空
     */
    private List<FileState> deltaList;
    private String deltaPath;
    private String basePath;
    private MetaDataManager metaDataManager;

    public CompactionTask(String modelPath,
                          AbstractClock clock,
                          Boolean toCompackBase,
                          List<FileState> streamList,
                          List<FileState> deltaList,
                          MetaDataManager metaDataManager) {
        this.clock = clock;
        this.toCompackBase = toCompackBase;
        this.streamList = streamList;
        this.deltaList = deltaList;

        this.deltaPath = modelPath + "delta/";
        this.basePath = modelPath + "base/";
        this.metaDataManager = metaDataManager;
    }

    @Override
    public void run() {
        try {
            compactDelta();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(toCompackBase) {
            compactbase();
        }
    }

    //TODO fill compaction logic
    private void compactDelta() throws IOException {
//        String deltaFilePath = buildDeltaFileName();
//        BufferedWriter writer = FileUtils.getFileWriter(deltaFilePath);
//        System.out.println("merging to delta from: " + metaDataManager.get
//        for (String streamFile : checkpoint.getStreamList()) {
//            writer.write(streamFile);
//            writer.newLine();
//            File file = new File(streamFile);
//            if (!file.delete()) {
//                throw new RuntimeException(file.getAbsolutePath() + " cannot be deleted");
//            }
//        }
//        checkpoint.clearAllStream();
//        writer.close();
//        checkpoint.addDelta(deltaFilePath);
//        dumpCheckpoint();
//        moveToNextHour();
    }
//
//    //TODO fill compaction logic
    private void compactbase() {
//        String currentBasePath = generateBaseFilePath();
//        checkpoint.setCurrentBasePath(currentBasePath);
//        BufferedWriter writer = FileUtils.getFileWriter(currentBasePath);
//        System.out.println("merging to base from: " + checkpoint.getDeltaList());
//        for (String deltaFile : checkpoint.getDeltaList()) {
//            writer.write(deltaFile);
//            writer.newLine();
//            File file = new File(deltaFile);
//            if (!file.delete()) {
//                throw new RuntimeException(file.getAbsolutePath() + " cannot be deleted");
//            }
//        }
//        checkpoint.clearAllDelta();
//        dumpCheckpoint();
//        writer.close();
//        moveToNextDay();
    }
}
