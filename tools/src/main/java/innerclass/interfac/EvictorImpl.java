package innerclass.interfac;


public class EvictorImpl implements Evictor {

    @Override
    public void evictBefore(EvictorContext context) {
        System.out.println("do evict before at " + context.getCurrentWatermark());
    }
}
