package base_delta_stream.easy_version;

import base_delta_stream.utils.FileUtils;
import com.alibaba.fastjson.JSONObject;
import utils.DateUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.util.Date;

/**
 * 编程实现：
 * 输入base_ts_0_0，以及计算当时时刻下一个M（minute），H（hour），D（day），（M存入M到M+1的数据，59分钟存59-下一个小时00的数据）
 * 创建M文件，每秒生成一个ts写入文件，时间到M，close文件，并把文件名加入到stream list中，并把M加1
 * 到H时，把stream list中的文件合并成一个delta文件，合成后删除stream文件，并清空stream list变量，把合成的delta文件加入delta list中，并把H加1
 * 到D时，把delta list中的文件合并成一个base文件，合成后删除delta文件，并清空delta list变量，并把D加1
 * 如果list为空，则合成空文件。对于M=H=D的情况，比如整点，需要先处理M，再处理H，最后处理D。
 *
 * TODO
 * case：meta已经存在完整的版本了，在写meta.tmp时进程挂了，需要从meta恢复，故应该保存meta中记录的文件。故过期策略应该是两个D之前的数据。
 * case：
 */
public class SeamlessAlignment {

    private static final String PATH_PREFIX = "/Users/fuyangyang/github/fyy_farming/tmp/";
    private static final String FIRST_BASE_PATH = PATH_PREFIX + "base0/";
    private static final String META_PATH = FIRST_BASE_PATH + "meta";
    private Checkpoint checkpoint = new Checkpoint();
    private long eventTime;
    private long currentMinute;
    private long currentHour;
    private long currentDay;
    private long nextMinute;
    private long nextHour;
    private long nextDay;

    /**
     * 写入文件的内容
     */
    private long number = 0;

    private BufferedWriter streamWriter;
    private String streamFilePath;

    private void start() throws Exception {
        initTime();

        while (true) {
            if (isMinute(eventTime)) {
                onMinute();

                if (isHour(eventTime)) {
                    onHour();
                }

                if (isDay(eventTime)) {
                    onDay();
                }
            } else {
                if(streamWriter == null) {
                    streamFilePath = generateStreamFilePath();
                    streamWriter = FileUtils.getFileWriter(streamFilePath);
                }
                streamWriter.write(String.valueOf(number++));
                streamWriter.newLine();
            }
            sleep(1);
            eventTime += 20 * 1000; //step 10 second
        }
    }

    private void onMinute() throws Exception {
        FileUtils.closeFile(streamWriter);
        checkpoint.addStream(streamFilePath);
        moveToNextMinute();
        streamFilePath = generateStreamFilePath();
        streamWriter = FileUtils.getFileWriter(streamFilePath);
        dumpCheckpoint();
    }

    /**
     * TODO write to tmp, then mv meta
     * @throws Exception
     */
    private void dumpCheckpoint() throws Exception {
        BufferedWriter fileWriter = FileUtils.getFileWriter(META_PATH);
        fileWriter.write(JSONObject.toJSONString(checkpoint));
        FileUtils.closeFile(fileWriter);
    }

    private void onHour() throws Exception {
        String deltaFilePath = generateDeltaFilePath();
        BufferedWriter writer = FileUtils.getFileWriter(deltaFilePath);
        System.out.println("merging to delta from: " + checkpoint.getStreamList());
        for(String streamFile : checkpoint.getStreamList()) {
            writer.write(streamFile);
            writer.newLine();
            File file = new File(streamFile);
            if(!file.delete()) {
                throw new RuntimeException(file.getAbsolutePath() + " cannot be deleted");
            }
        }
        checkpoint.clearAllStream();
        writer.close();
        checkpoint.addDelta(deltaFilePath);
        dumpCheckpoint();
        moveToNextHour();
    }

    /**
     * 一天触发一次
     * @throws Exception
     */
    private void onDay() throws Exception {
        String currentBasePath = generateBaseFilePath();
        checkpoint.setCurrentBasePath(currentBasePath);
        BufferedWriter writer = FileUtils.getFileWriter(currentBasePath);
        System.out.println("merging to base from: " + checkpoint.getDeltaList());
        for(String deltaFile : checkpoint.getDeltaList()) {
            writer.write(deltaFile);
            writer.newLine();
            File file = new File(deltaFile);
            if(!file.delete()) {
                throw new RuntimeException(file.getAbsolutePath() + " cannot be deleted");
            }
        }
        checkpoint.clearAllDelta();
        dumpCheckpoint();
        writer.close();
        moveToNextDay();
    }

    private boolean isMinute(long ts) {
        return ts > nextMinute;
    }

    private boolean isHour(long ts) {
        return ts > nextHour;
    }

    private boolean isDay(long ts) {
        return ts > nextDay;
    }

    private void initTime() {
        Date date = new Date();
        long now = date.getTime();
        System.out.println(now);

        currentMinute = now - now % 60000;
        nextMinute = currentMinute + 60000;
        System.out.println("current Minute:" + DateUtils.getTime(currentMinute));
        System.out.println("next    Minute:" + DateUtils.getTime(nextMinute));

        currentHour = now - now % (60 * 60000);
        nextHour = currentHour + 60 * 60000;
        System.out.println("current Hour:" + DateUtils.getTime(currentHour));
        System.out.println("next    Hour:" + DateUtils.getTime(nextHour));

        currentDay = now - now % (24 * 60 * 60000);
        nextDay = currentDay + 24 * 60 * 60000;
        System.out.println("current Day:" + DateUtils.getTime(currentDay));
        System.out.println("next    Day:" + DateUtils.getTime(nextDay));

        eventTime = System.currentTimeMillis();
    }

    private void sleep(int interval) {
        try {
            Thread.sleep(interval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String generateStreamFilePath() {
        return FIRST_BASE_PATH + "stream/stream_" + DateUtils.getTime(currentMinute, "yyyy-MM-dd-HH-mm-ss");
    }

    private String generateDeltaFilePath() {
        return FIRST_BASE_PATH + "delta/delta_" + DateUtils.getTime(currentHour, "yyyy-MM-dd-HH-mm-ss");
    }

    private String generateBaseFilePath() {
        return FIRST_BASE_PATH + "base/base_" + DateUtils.getTime(currentDay, "yyyy-MM-dd-HH-mm-ss");
    }

    private void moveToNextMinute() {
        currentMinute = nextMinute;
        nextMinute += 60000;
    }

    private void moveToNextHour() {
        currentHour = nextHour;
        nextHour += 60 * 60000;
        System.out.println("currentHour:" + DateUtils.getTime(currentHour));
        System.out.println("nextHour:" + DateUtils.getTime(nextHour));
    }

    private void moveToNextDay() {
        currentDay = nextDay;
        nextDay += 24 * 60 * 60000;
        System.out.println("currentDay:" + DateUtils.getTime(currentDay));
        System.out.println("nextDay:" + DateUtils.getTime(nextDay));
    }

    public static void main(String[] args) throws Exception {
        SeamlessAlignment manager = new SeamlessAlignment();
        manager.start();
    }
}
