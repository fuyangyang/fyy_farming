package base_delta_stream.core.metadata;

import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Metadata {
    private Long eventTime;
    private List<FileDesc> baseList;
    private List<FileDesc> deltaList;
    private List<String> streamList;

    public Metadata(Long eventTime, List<FileDesc> baseList, List<FileDesc> deltaList, List<String> streamList) {
        this.eventTime = eventTime;
        this.baseList = baseList;
        this.deltaList = deltaList;
        this.streamList = streamList;
    }

    public Long getEventTime() {
        return eventTime;
    }

    public List<FileDesc> getBaseList() {
        return baseList;
    }

    public List<FileDesc> getDeltaList() {
        return deltaList;
    }

    public List<String> getStreamList() {
        return streamList;
    }

    public static class FileDesc {
        private String fileName;
        private Status status;
        private String lastCompactedFileName;

        public FileDesc(String fileName, Status status, String lastCompactedFileName) {
            this.fileName = fileName;
            this.status = status;
            this.lastCompactedFileName = lastCompactedFileName;
        }

        public String getFileName() {
            return fileName;
        }

        public Status getStatus() {
            return status;
        }

        public String getLastCompactedFileName() {
            return lastCompactedFileName;
        }

        public enum Status {
            WRITING,
            OK,
            COMPACTED,
            ROLLBACK,
            EMPTY
        }

    }

    public static void main(String[] args) {
        List <FileDesc> baseList = new LinkedList<>();
        List <FileDesc> deltaList = new LinkedList<>();
        List <String> streamList = new LinkedList<>();
        FileDesc base04 = new FileDesc("base_2019-06-04_0_1000", FileDesc.Status.OK, "delta_2019-06-04-23_900_1000");
        baseList.add(base04);
        FileDesc base05 = new FileDesc("base_2019-06-05_0_2000", FileDesc.Status.OK, "delta_2019-06-05-23_1900_2000");
        baseList.add(base05);

        FileDesc delta01 = new FileDesc("delta_2019-06-06-00_2000_2100", FileDesc.Status.OK, "stream_2019-06-06-00-59-00_2090_2100");
        FileDesc delta02 = new FileDesc("delta_2019-06-06-01_2100_2200", FileDesc.Status.OK, "stream_2019-06-06-01-59-00_2190_2200");
        deltaList.add(delta01);
        deltaList.add(delta02);

        streamList.add("stream_2019-06-06-02-00-00_2200_2210");
        streamList.add("stream_2019-06-06-02-01-00_2210_2220");
        Metadata metadata = new Metadata(1559736262L, baseList, deltaList, streamList);
        System.out.println(JSONObject.toJSONString(metadata));
    }

}
