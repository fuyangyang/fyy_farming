package guava.retry;

import com.github.rholder.retry.*;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RetryerCreator {
    private final static Logger LOG = LoggerFactory.getLogger(RetryerCreator.class);
    private final static RetryListener DFT_LISTENER = new RetryListener() {
        @Override
        public <V> void onRetry(Attempt<V> attempt) {
            if (attempt.hasException()) {
                LOG.error("Exception: ", attempt.getExceptionCause());
            }
        }
    };

    public static Retryer<Boolean> createFinityRetryer(int sleepTime, int attemptNumber) {
        return createFinityRetryer(sleepTime, attemptNumber, DFT_LISTENER);
    }

    public static Retryer<Boolean> createFinityRetryer(int sleepTimeMs, int attemptNumber, RetryListener listener) {
        return RetryerBuilder
                .<Boolean>newBuilder()
                .retryIfException()
                .retryIfResult(Predicates.equalTo(false))
                .withWaitStrategy(WaitStrategies.fixedWait(sleepTimeMs, TimeUnit.MILLISECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(attemptNumber))
                .withRetryListener(listener)
                .build();
    }

    public static Retryer<Boolean> createInfiniteRetryer(int sleepTime) {
        return createInfiniteRetryer(sleepTime, DFT_LISTENER);
    }

    public static Retryer<Boolean> createInfiniteRetryer(int sleepTimeMs, RetryListener listener) {
        Preconditions.checkNotNull(listener, "RetryListener is required");
        return RetryerBuilder
                .<Boolean>newBuilder()
                .retryIfException()
                .retryIfResult(Predicates.equalTo(false))
                .withWaitStrategy(WaitStrategies.fixedWait(sleepTimeMs, TimeUnit.MILLISECONDS))
                .withStopStrategy(StopStrategies.neverStop())
                .withRetryListener(listener)
                .build();
    }


    private static final Retryer<Boolean> infiniteRetryer = RetryerCreator.createInfiniteRetryer(3000);
    private static final Retryer<Boolean> finiteRetryer = RetryerCreator.createFinityRetryer(3000, 3);

    //test
    public static void testInfiniteRetryer() throws Exception {
        infiniteRetryer.call(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int x = 1/0;
                return true;
            }
        });
    }

    //test
    public static void testFiniteRetryer() throws Exception {
        finiteRetryer.call(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int x = 1/0;
                return true;
            }
        });
    }
}
