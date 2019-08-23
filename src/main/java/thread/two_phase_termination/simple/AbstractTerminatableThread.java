package thread.two_phase_termination.simple;

import thread.two_phase_termination.Terminatable;

public abstract class AbstractTerminatableThread extends Thread implements Terminatable {

    private volatile boolean toShutdown;

    public AbstractTerminatableThread() {
        toShutdown = false;
    }

    /**
     * 用户逻辑
     * @throws Exception
     */
    protected abstract void doRun() throws Exception;

    protected void doCleanUp(Exception cause) {
    }

    protected void doTerminate() {
    }

    @Override
    public void run() {
        Exception ex = null;
        try {
            while (true) {
                if (toShutdown) {
                    break;
                }
                doRun();
            }
        } catch (Exception e) {
            ex = e;
        } finally {
            doCleanUp(ex);
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    @Override
    public void terminate() {
        toShutdown = true;//标记位，代码会检测这个标记并作出反应
        doTerminate();//对interrupt方法无响应的阻塞方法，比如socket.read()，手动关闭socket
        super.interrupt();//能对interrupt有响应的方法
    }

    public void terminate(boolean waitUntilThreadTerminated) {
        terminate();
        if (waitUntilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
