package base_delta_stream;

import com.google.common.base.Preconditions;

public class OffsetPair {

    /**
     *
     */
    private Long start;

    /**
     *
     */
    private Long end;

    public void setOffset(Long offset) {
        if (start == null) {
            start = offset;
        }
        end = offset;
    }

    public Long getStart() {
        Preconditions.checkNotNull(start, "start offset must be set!");
        return start;
    }

    public Long getEnd() {
        return end;
    }

    public void clear() {
        start = null;
        end = null;
    }
}
