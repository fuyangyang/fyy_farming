package fyy.shutdownhook;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ShutdownHookQueue {

    private static BlockingDeque<String> queue = new LinkedBlockingDeque();

    static class CleanWorkThread extends Thread{
        @Override
        public void run() {
            System.out.println("cleaning");
            int i = 0;
            while (!queue.isEmpty()) {
                String poll = queue.poll();
                System.out.println("out" + (i++));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new CleanWorkThread());
        for (int i = 0; i < 100000; i++) {
//            queue.offer(String.valueOf(i));
            System.out.println(i);
            queue.offer(getMessage());
            Thread.sleep(1);
        }
    }

    private static String getMessage() {
        String content = "www.javaseo.cn";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append(content);
        }
//        System.out.println(sb.length());
        return sb.toString();
    }
}
