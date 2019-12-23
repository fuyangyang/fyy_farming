package http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MyHttpServer {

    public static void main(String[] args) throws Exception {
        //创建http服务器，绑定本地8888端口*
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8089), 0);
        //创建上下文监听,拦截包含/test的请求*
        httpServer.createContext("/test", new TestHttpHandler());
        httpServer.start();
    }

    private static class TestHttpHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "test message";
//            response = "<!doctype html>\n" +
//                    "<html>\n" +
//                    "<head>\n" +
//                    "<title>div分区元素</title>\n" +
//                    "<meta charset=\"utf-8\" />\n" +
//                    "</head>\n" +
//                    "<body>\n" +
//                    "  <!-- logo/工具 -->\n" +
//                    "  <div style=\"border: 1px solid yellow;\">\n" +
//                    "    <h1>这里放置logo和按钮</h1>\n" +
//                    "  </div>\n" +
//                    "  <!-- 内容 -->\n" +
//                    "  <div style=\"border: 1px solid yellow;\">\n" +
//                    "    <p>Hello.</p>\n" +
//                    "    <p>How are you.</p>\n" +
//                    "    <p>And you?</p>\n" +
//                    "  </div>\n" +
//                    "  <!-- 版权标识 -->\n" +
//                    "  <div style=\"border: 1px solid yellow;\">\n" +
//                    "    <p>盗版必究</p>\n" +
//                    "  </div>\n" +
//                    "</body>\n" +
//                    " \n" +
//                    "</html>";
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("UTF-8"));
            os.close();
        }
    }
}
