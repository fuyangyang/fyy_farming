package innerclass.interfac;

import java.io.Serializable;

public interface Evictor extends Serializable {
    void evictBefore(EvictorContext context);

    interface EvictorContext {
        long getCurrentWatermark();
    }
}
