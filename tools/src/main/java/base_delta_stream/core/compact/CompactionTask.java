package base_delta_stream.core.compact;

import base_delta_stream.core.metadata.MetaDataManager;
import base_delta_stream.utils.FileUtils;
import utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

/**
 * 合并任务输出：
 *  * 1）合并成功后，把manifest中的被合并的文件状态置为true，更新metadata。然后把list提交到清除队列，让fc线程清除文件。
 */
public class CompactionTask {

    /**
     * 是否触发base合并，如果触发，必须先合并完delta后再合并base。
     */
    private Boolean toCompackBase;

    /**
     * 待合并到delta的stream list, 现在是正序排列
     */
    private List<String> streamList;

    /**
     * 待合并到base的delta list，如果不触发base合并，本参数可为空
     */
    private List<String> deltaList;
    private String deltaPathPrefix;
    private String basePathPrefix;
    private MetaDataManager metaDataManager;

    /**
     * 用于生成文件名称
     */
    private Long currentHour;

    public CompactionTask(Long currentHour,
                          String modelPath,
                          Boolean toCompackBase,
                          List<String> streamList,
                          List<String> deltaList,
                          MetaDataManager metaDataManager) {
        this.currentHour = Long.valueOf(currentHour);
        this.toCompackBase = toCompackBase;
        this.streamList = streamList;
        this.deltaList = deltaList;

        this.deltaPathPrefix = modelPath + "delta/";
        this.basePathPrefix = modelPath + "base/";
        this.metaDataManager = metaDataManager;
    }

    public void compact() {
        try {
            compactDelta();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(toCompackBase) {
            compactbase();
        }
    }

    private void compactDelta() throws IOException {
        String writingDeltaFileName = deltaPathPrefix + DateUtils.getTime(currentHour, "yyyy-MM-dd-HH-mm-ss") + ".writing";
        System.out.println("merging to delta from: " + writingDeltaFileName);
        String[] split = streamList.get(0).split("_");
        String startOffset = split[split.length - 2];
        split = streamList.get(streamList.size() - 1).split("_");
        String endOffset = split[split.length - 1];
        BufferedWriter fileWriter = FileUtils.getFileWriter(writingDeltaFileName);
        for (String streamFile : streamList) {
            fileWriter.write(streamFile);
            fileWriter.newLine();
        }
        fileWriter.close();
        String deltaFilePath = writingDeltaFileName.split("\\.")[0] + "_" + startOffset + "_" + endOffset;
        FileUtils.renameTo(writingDeltaFileName, deltaFilePath);

        //把所有的streamlist设置为被合并
        metaDataManager.setStreamCompacted(streamList);
        metaDataManager.addDeltaAndDump(deltaFilePath);
    }

    private void compactbase() {
        System.out.println("compactbase not implemented yet");


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
