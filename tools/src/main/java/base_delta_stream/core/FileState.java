package base_delta_stream.core;


/**
 * 文件状态
 */
public class FileState {

    /**
     * 文件相对路径
     */
    private String path;

    /**
     * 文件是否被合并过
     */
    private Boolean isCompacted;

    public FileState(String path) {
        this.path = path;
        this.isCompacted = false;
    }

    public String getPath() {
        return path;
    }

    public Boolean getCompacted() {
        return isCompacted;
    }

    public void compacted() {
        isCompacted = true;
    }
}
