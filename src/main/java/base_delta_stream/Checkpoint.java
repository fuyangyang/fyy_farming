package base_delta_stream;

import java.util.ArrayList;
import java.util.List;

public class Checkpoint {
    private String currentBasePath;
    private List<String> deltaList = new ArrayList<>();
    private List<String> streamList = new ArrayList<>();

    public String getCurrentBasePath() {
        return currentBasePath;
    }

    public void setCurrentBasePath(String currentBasePath) {
        this.currentBasePath = currentBasePath;
    }

    public List<String> getDeltaList() {
        return deltaList;
    }

    public void addDelta(String delta) {
        deltaList.add(delta);
    }

    public void clearAllDelta() {
        deltaList.clear();
    }

    public List<String> getStreamList() {
        return streamList;
    }

    public void addStream(String stream) {
        streamList.add(stream);
    }

    public void clearAllStream() {
        streamList.clear();
    }

}
