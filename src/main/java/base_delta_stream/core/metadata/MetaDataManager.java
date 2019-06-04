package base_delta_stream.core.metadata;

import base_delta_stream.core.FileState;
import base_delta_stream.utils.FileUtils;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.List;


/**
 * keep meta in memory and disk synchronously;
 */
public class MetaDataManager {

    private Manifest manifest;
    private String path;


    public MetaDataManager(String basePath) {
        this.manifest = new Manifest();
        this.path = basePath + "manifest";
    }

    public synchronized void addStream(String path) {
        addStream(new FileState(path));
    }

    public synchronized void addStream(FileState state) {
        manifest.addStream(state);
    }

    public synchronized void addDelta(String path) {
        addDelta(new FileState(path));
    }

    public synchronized void addDelta(FileState state) {
        manifest.addDelta(state);
    }

    public synchronized void addStreamAndDump(String path) {
        addStream(path);
        dump();
    }

    public synchronized void addDeltaAndDump(String deltaFilePath) {
        addDelta(deltaFilePath);
        dump();
    }

    public synchronized void dump() {
        File file = new File(path + ".tmp");
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(JSONObject.toJSONString(manifest));
            FileUtils.closeFile(fileWriter);
            file.renameTo(new File(path));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setStreamCompacted(List<String> streamList) {
        manifest.setStreamCompacted(streamList);
    }

    //TODO 这个方法干什么用，要同步吗
    public FileState findFrontStream(String stream) {
        List<FileState> streamList = manifest.getStreamList();
        if (streamList.isEmpty()) {
            return null;
        }

        FileState front = streamList.get(0);
        streamList.remove(0);
        return front;
    }



    public String setDeltaCompacted(boolean compacted) {
        return null;
    }

    public List<String> findStreamsByDelta(String delta) {
        return null;
    }

    public List<String> findDeltasByBase(String base) {
        return null;
    }

    public List<String> findExpired(long timeExpired) {
        return null;
    }

    public String findLatestStreamByBase(String base) {
        return null;
    }

    public String findLatestStreamByDelta(String delta) {
        return null;
    }


}
