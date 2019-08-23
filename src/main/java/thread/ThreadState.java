package thread;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * 验证：等待IO，是RUNNABLE状态
 */
public class ThreadState {

    //sleep中的状态是timed_waiting
//    public static void main(String[] args) throws InterruptedException {
//        TimeUnit.MINUTES.sleep(1);
//    }

    /**
     * 等待IO，是RUNNABLE状态
     *
     * "main" #1 prio=5 os_prio=31 tid=0x00007fc7e980d000 nid=0x1903 runnable [0x0000700002427000]
     *    java.lang.Thread.State: RUNNABLE
     * 	at java.net.PlainSocketImpl.socketAccept(Native Method)
     * 	at java.net.AbstractPlainSocketImpl.accept(AbstractPlainSocketImpl.java:409)
     * 	at java.net.ServerSocket.implAccept(ServerSocket.java:545)
     * 	at java.net.ServerSocket.accept(ServerSocket.java:513)
     * 	at thread.ThreadState.run(ThreadState.java:52)
     * 	at thread.ThreadState.main(ThreadState.java:20)
     * @param args
     */
//    public static void main(String[] args) {
//        new ThreadState().run();
//    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                new ThreadState().run();
            }
        });

        t.setDaemon(false);
        t.start();

        TimeUnit.SECONDS.sleep(1);

        t.interrupt();
    }

    public static final int SMALL_BUF_SIZE = 8;
    public static final int PORT = 8080;
    public static final int BACK_LOG = 50;
    // client can use http get uri to close server, eg: http://localhost:8080/stop
    private static final String STOP_URL = "/stop";
    // if client stop server, the string of response
    private static final String CLOSE_RESP_STR = "Server Close";
    private static volatile boolean stop = false;

    // The html template of response
    private static final String HTML = "HTTP/1.1 200 OK\r\n"
            + "Content-Type: text/html\r\n"
            + "Content-Length: %d\r\n" + "\r\n"
            + "%s";

    public void run() {
        ServerSocket server = null;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(PORT), BACK_LOG);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Socket client = null;
        InputStream is = null;
        OutputStream os = null;
        while (!stop) {
            try {
                client = server.accept();
                is = client.getInputStream();
                os = client.getOutputStream();

                // handle inputStream
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder reqStr = new StringBuilder();
                char[] buf = new char[SMALL_BUF_SIZE];
                do {
                    if (br.read(buf) != -1) {
                        reqStr.append(buf);
                    }
                } // the key point to read a complete arrival socket stream with bio but without block
                while (br.ready());

                // get uri in http request line
                String respStr = parse(reqStr.toString());

                // handle outputStream
                if (stop = STOP_URL.equalsIgnoreCase(respStr)) {
                    respStr = CLOSE_RESP_STR;
                    System.out.println("client require server to stop");
                }

                // join the html content
                respStr = "<h1>" + respStr + "</h1>";
                os.write(String.format(HTML, respStr.length(), respStr).getBytes());
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            server.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String parse(String source) {
        if(source == null || source.length() == 0) {
            return new String();
        }

        int startIndex;
        startIndex = source.indexOf(' ');
        if (startIndex != -1) {
            int paramIndex = source.indexOf('?', startIndex + 1);
            int secondBlankIndex = source.indexOf(' ', startIndex + 1);
            int endIndex = -1;
            if (secondBlankIndex > paramIndex) {
                endIndex = secondBlankIndex;
            } else {
                endIndex = paramIndex;
            }
            if (endIndex > startIndex)
                return source.substring(startIndex + 1, endIndex);
        }
        return new String();
    }


}
