package base_delta_stream.source;

import base_delta_stream.core.Message;

import static java.lang.Thread.sleep;

/**
 * mock kafka
 * emit a message per second
 */
public class KafkaMessageSource {

    private long currentOffset;
    private long currentTimestamp;
    private long endTimestamp;
    private static final long EMIT_NUM = 100000000L;

    public KafkaMessageSource(long startOffset, long startTimestamp, long emitNum) {
        this.currentOffset = startOffset;
        this.currentTimestamp = startTimestamp;
        endTimestamp = currentTimestamp + emitNum;
    }

    public KafkaMessageSource(long startOffset) {
        this.currentOffset = startOffset;
        this.currentTimestamp = System.currentTimeMillis();
        this.endTimestamp = currentTimestamp + EMIT_NUM;
    }

    public KafkaMessageSource() {
        this(0);
    }

    public Message get() {
        Message msg = Message.build(currentOffset, currentTimestamp, currentOffset);
        currentOffset++;
        currentTimestamp += 1000;
        try {
            sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(currentOffset % 60 == 0) {
            currentTimestamp += 70 * 1000;
        }
        return msg;
    }

    public boolean hasNext() {
        return currentTimestamp <= endTimestamp;
    }

}
