package thread.two_phase_termination.with_token;

import thread.two_phase_termination.Terminatable;

public abstract class AbstractTerminatableThread extends Thread implements Terminatable {

    private TerminationToken terminationToken;

    public AbstractTerminatableThread() {
        this(new TerminationToken());
    }
    public AbstractTerminatableThread(TerminationToken terminationToken) {
        this.terminationToken = terminationToken;
        terminationToken.register(this);
    }

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
                if (terminationToken.isToShutdown()
                        && terminationToken.reservations.get() <= 0) {
                    break;
                }
                doRun();
            }
        } catch (Exception e) {
            ex = e;
        } finally {
            try {
                doCleanUp(ex);
            } finally {
                terminationToken.notifyThreadTermination(this);
            }
        }
    }

    @Override
    public void interrupt() {
        terminate();
    }

    @Override
    public void terminate() {
        terminationToken.setToShutdown(true);
        try {
            doTerminate();
        } finally {
            if (terminationToken.reservations.get() <= 0) {
                super.interrupt();
            }
        }
    }

    public void terminate(boolean waitUntilThreadTerminated) {
        terminate();
        if(waitUntilThreadTerminated) {
            try {
                this.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
