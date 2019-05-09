package thread;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * call方法里抛出的异常不会打印出来，故一定要在call里加一个大的try catch接异常，并打印
 *
 * main线程在提交完任务就正常退出了，子线程遇到异常会退出，但是进程不退出，观察线程后，发现有2类非daemon线程，包括：
 * 1. 线程池里的8个线程
 * 2. DestroyJavaVM线程：main线程结束以后，会生成一个DestroyJavaVM线程，用于在所有非daemon线程退出后，结束jvm进程
 *    ref https://lvdccyb.iteye.com/blog/1779752
 *    在main函数结束后，虚拟机会自动启动一个DestroyJavaVM线程，该线程会等待所有user thread 线程结束后退出（即，只剩下daemon 线程和DestroyJavaVM线程自己，整个虚拟机就退出，此时daemon线程被终止），因此，如果不希望程序退出，只要创建一个非daemon的子线程，让线程不停的sleep即可
 *
 * 线程池本身是一个对象，不是一个线程，池里维护的线程才是一个个线程
 *
 * 线程池提交的任务，不管是runnable还是callable任务，不管任务执行成功或失败，newFixedThreadPool池中的线程个数不变。
 */
public class GuavaExecutors {
    private static final int THREAD_POOL_SIZE = 8;
    private ListeningExecutorService executors = MoreExecutors.listeningDecorator(
            Executors.newFixedThreadPool(THREAD_POOL_SIZE));
    private static final int REQUEST_TIMEOUT_SEC = 600;

    public static void main(String[] args) {

        new GuavaExecutors().submitRunnableTasks();
//        while (true) {
//            try {
//                TimeUnit.SECONDS.sleep(1111);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void submitRunnableTasks() {
        List<ListenableFuture<?>> reqPsFutures = Lists.newArrayList();
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            ListenableFuture<?> listenableFuture = executors.submit(new MyRunnable(("task-" + i)));
            reqPsFutures.add(listenableFuture);
        }

        ListenableFuture<List<Object>> result = Futures.successfulAsList(reqPsFutures);

        try {
            List<Object> totalResult = result.get(REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS);
            if (totalResult.size() != THREAD_POOL_SIZE) {
                System.out.println("not equal");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        reqPsFutures.clear();
        System.out.println("submitRunnableTasks run over");
    }

    public void submitCallableTasks() {
        List<ListenableFuture<Boolean>> reqPsFutures = Lists.newArrayList();
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            ListenableFuture<Boolean> listenableFuture = executors.submit(new MyTask("task-" + i));
            reqPsFutures.add(listenableFuture);
        }

        Future<List<Boolean>> result = Futures.successfulAsList(reqPsFutures);

        try {
            List<Boolean> totalResult = result.get(REQUEST_TIMEOUT_SEC, TimeUnit.SECONDS);
            if (totalResult.size() != THREAD_POOL_SIZE) {
                System.out.println("not equal");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        reqPsFutures.clear();
        System.out.println("submitCallableTasks run over");
    }

    private static class MyTask implements Callable<Boolean> {
        private String taskName;

        public MyTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public Boolean call() throws Exception {

            for (int i = 0; i < 10; i++) {
                System.out.println(taskName + " i=" + i);
//                try {
                    int x = 1 / 1;
//                    int x = 1 / 0;
//                } catch (Exception e) {
//                    System.out.println("task:" + taskName);
//                    e.printStackTrace();
//                }

            }
            return true;
        }
    }

    private static class MyRunnable implements Runnable {
        private String taskName;

        public MyRunnable(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                System.out.println(taskName + " i=" + i);
//                try {
                int x = 1 / 1;
//                int x = 1 / 0;
//                } catch (Exception e) {
//                    System.out.println("task:" + taskName);
//                    e.printStackTrace();
//                }

            }
        }
    }



}
