package fqueue;

import com.github.cgdon.fqueue.FQueue;

public class FQueueThreadSafeTest {
    public static void main(String[] args) throws Exception {
        int loop = 1000;

        FQueue fQueue = new FQueue("/Users/fuyangyang/IdeaProjects/mlx_ps_fetcher/dbthreadsafe/");
        byte[] message = getMessage();
        long offerStart = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            fQueue.offer(String.valueOf(i).getBytes());
        }
        long offerEnd = System.currentTimeMillis();
//        System.out.println((double)loop / ((offerEnd - offerStart) / 1000) + "qps");
        System.out.println((offerEnd - offerStart) + "ms cost");

        long pollStart = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            byte[] received = fQueue.poll();
            System.out.println(new String(received));
        }
        long poolEnd = System.currentTimeMillis();
//        System.out.println((double)loop / ((poolEnd - pollStart) / 1000) + "qps");
        System.out.println((poolEnd - pollStart) + "ms");
        fQueue.close();
    }

    private static byte[] getMessage() {
        String content = "www.javaseo.cn";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append(content);
        }
        System.out.println(sb.length());
        return sb.toString().getBytes();
    }
}
