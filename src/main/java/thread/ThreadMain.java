package thread;

/**
 * 用来测试线程和主线程的关系
 *
 */
public class ThreadMain {

    public static void main(String[] args) {
        test3();
    }

    /**
     * 测试deamon
     * 结论：见代码中的注释
     */
    public static void test3() {
        Thread t = new Thread("child") {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("child run");
                }
            }
        };

        //如果设置为true，那么主线程退出后，进程就退出了; 设置为false，主线程退出后，子线程还在跑，进程也不退出。线程默认是非守护的。
        //进程退出条件，最后一个非守护线程退出时，进程才退出
        t.setDaemon(false);
        t.start();
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("main thread is finished");
    }

    /**
     * case1: 子线程异常退出，主线程还在吗，进程还在吗？
     * 结论：只有子线程退出，主线程还在，两者没有半毛钱关系，进程也还在
     */
    public static void test1() {
        new Thread("child") {
            @Override
            public void run() {
                while(true) {
                    System.out.println("child run");
                    int x = 1 / 0;
                }
            }
        }.start();

        for (int i = 0; i < 10000; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("main thread print: i=" + i);
        }
    }

    /**
     * 主线程接收子线程的异常
     * 如果不在主线程中加setUncaughtExceptionHandler处理异常，主线程不会得到子线程的异常。
     */
    public static void test2() {
        Thread t = new Thread("child") {
            @Override
            public void run() {
                while(true) {
                    System.out.println("child run");
                    int x = 1 / 0;
                }
            }
        };

        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("uncaughtException in main thread:");
                e.printStackTrace();
                /**
                 * 可以在这进一步处理：如再起一个补充线程或者释放资源
                 */
            }
        });

        t.start();

        for (int i = 0; i < 10000; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("main thread print: i=" + i);
        }

    }
}
