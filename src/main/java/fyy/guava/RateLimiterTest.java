package fyy.guava;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 参考:http://ifeve.com/guava-ratelimiter/
 * Created by fuyi on 17/12/13.
 */
public class RateLimiterTest {

    private static final RateLimiter rateLimiter = RateLimiter.create(0.5); //每秒产生0.5个token

    public static void main(String[] args) {
//        testOneByOne(10);
        testByStep(10, 2);
    }

    private static void testOneByOne(int num) {
        testByStep(10, 1);
    }

    private static void testByStep(int num, int step) {
        for (int i = 0; i < num; i++) {
            double acquire = rateLimiter.acquire(step);
            System.out.println(System.currentTimeMillis() / 1000 + ":" + acquire);
        }
    }
}
