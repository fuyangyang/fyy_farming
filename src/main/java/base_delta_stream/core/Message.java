package base_delta_stream.core;


/**
 * 封装成TimedMessage
 * @param <T>
 */
public class Message<T> {
    private long offset;
    private long timestamp;
    private T content;

    public Message(long offset, long timestamp, T content) {
        this.offset = offset;
        this.timestamp = timestamp;
        this.content = content;
    }

    public static <T> Message build(long offset, long timestamp, T content) {
        return new Message(offset, timestamp, content);
    }

    public long getOffset() {
        return offset;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public T getContent() {
        return content;
    }
}
