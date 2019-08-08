package innerclass.interfac;

public class Op {
    private Evictor evictor;
    private EvictorContext evictorContext;
    private long watermark;

    public Op(Evictor evictor) {
        this.evictor = evictor;
        this.evictorContext = new EvictorContext();
    }

    public void processElement(long ts) {
        watermark = ts;
        evictor.evictBefore(evictorContext);
    }

    class EvictorContext implements Evictor.EvictorContext {
        @Override
        public long getCurrentWatermark() {
            return watermark;
        }
    }

    public static void main(String[] args) {
        Op op = new Op(new EvictorImpl());
        for (int i = 0; i < 10; i++) {
            op.processElement(i);
        }
    }
}
