package base_delta_stream.core.metadata;

import base_delta_stream.core.FileState;

import java.util.LinkedList;
import java.util.List;

//TODO 可能需要细化，每个delta对应的stream list，每个base对应的delta list，这样才更加清晰，延迟问题也能很好解决。或者再借鉴下levelDB的元数据管理经验。
public class Manifest {

    private List<FileState> baseList = new LinkedList<>();
    private List<FileState> deltaList = new LinkedList<>();
    private List<FileState> streamList = new LinkedList<>();//可以换成treemap，以方便改状态

    public Manifest() {
    }

    public List<FileState> getBaseList() {
        return baseList;
    }

    public List<FileState> getDeltaList() {
        return deltaList;
    }

    public List<FileState> getStreamList() {
        return streamList;
    }

    public void clearAllDelta() {
        deltaList.clear();
    }

    public void addStream(FileState stream) {
        streamList.add(0, stream);
    }

    public void addDelta(FileState fileState) {
        deltaList.add(0, fileState);
    }

    public void clearAllStream() {
        streamList.clear();
    }

    public void setStreamCompacted(List<String> list) {
        for (String stream : list) {
            for (FileState fileState : streamList) {
                if (fileState.getPath().equals(stream)) {
                    fileState.compacted();
                }
            }
        }
    }


}
