package guava;


import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * ref https://www.cnblogs.com/jianzh5/p/6651799.html
 */
public class RetryerCreator {

    private static final int RETRY_TIMES_AT_MOST = 3;
    private static final int RETRY_WAITING_SECONDS = 1;

    static Callable<Boolean> callable = new Callable<Boolean>() {
        private int times = 0;

        @Override
        public Boolean call() throws Exception {
            times++;
            System.out.println("retry " + times + " times at " + new Date());
            if (times > RETRY_TIMES_AT_MOST) {
                return true;
            } else {
                int x = 3 / 0;
                return false;
            }
        }
    };
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        retryFiniteTimes();

    }

    public static void retryInfiniteTimes() {

    }

    public static void retryFiniteTimes() {
        try {
            createFiniteRetryer().call(callable);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            e.printStackTrace();
        }
    }

    private static Retryer<Boolean> createFiniteRetryer() {
        return RetryerBuilder
                .<Boolean>newBuilder()
                //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
                .retryIfException()
                //返回false也需要重试
                .retryIfResult(Predicates.equalTo(false))
                //重调策略
                .withWaitStrategy(WaitStrategies.fixedWait(RETRY_WAITING_SECONDS, TimeUnit.SECONDS))
                //尝试次数
                .withStopStrategy(StopStrategies.stopAfterAttempt(RETRY_TIMES_AT_MOST))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.hasException()) {
                            System.out.println(ExceptionUtils.getStackTrace(attempt.getExceptionCause()));
                        }
                    }
                })
                .build();
    }

    private static Retryer<Boolean> createInfiniteRetryer() {
        return RetryerBuilder
                .<Boolean>newBuilder()
                //抛出runtime异常、checked异常时都会重试，但是抛出error不会重试。
                .retryIfException()
                //返回false也需要重试
                .retryIfResult(Predicates.equalTo(false))
                //重调策略
                .withWaitStrategy(WaitStrategies.fixedWait(RETRY_WAITING_SECONDS, TimeUnit.SECONDS))
                //尝试次数
                .withStopStrategy(StopStrategies.neverStop())
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.hasException()) {
                            System.out.println(ExceptionUtils.getStackTrace(attempt.getExceptionCause()));
                        }
                    }
                })
                .build();
    }

}
