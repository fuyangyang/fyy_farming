package signal;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * Created by fuyi on 17/2/3.
 */
public class SignalHandlerExample implements SignalHandler {
    private SignalHandler oldHandler;

    public static SignalHandler install(String signalName) {
        Signal diagSignal = new Signal(signalName);
        SignalHandlerExample instance = new SignalHandlerExample();
        instance.oldHandler = Signal.handle(diagSignal, instance);
        return instance;
    }

    @Override
    public void handle(Signal signal) {
        System.out.println("Signal handler called for signal "
                + signal);
        try {

            signalAction(signal);

            // Chain back to previous handler, if one exists
            if (oldHandler != SIG_DFL && oldHandler != SIG_IGN) {
                oldHandler.handle(signal);
            }

        } catch (Exception e) {
            System.out.println("handle|Signal handler failed, reason " + e.getMessage());
                    e.printStackTrace();
        }
    }

    public void signalAction(Signal signal) {
        System.out.println("Handling " + signal.getName());
        System.out.println("Just sleep for 5 seconds.");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: "
                    + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SignalHandlerExample.install("TERM");
        SignalHandlerExample.install("INT");

        System.out.println("Signal handling example.");
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }

    }
}
