package http;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.http.client.methods.HttpGet;

import java.util.concurrent.Future;

public class MyAsyncHttpClient {
    public static void main(String[] args) {


        AsyncHttpClient client = new AsyncHttpClient();
        HttpGet httpGet = new HttpGet("http://localhost:8089/test");
        try {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
//                Future<Response> f = client.prepareGet("http://localhost:8089/test").execute();
                Future<Response> f = client.prepareGet("http://localhost:8089/test").execute(new AsyncCompletionHandler() {
                    @Override
                    public Object onCompleted(Response response) throws Exception {
//                        System.out.println("完成请求");
                        return response;
                    }
                });
//                System.out.println(f.get().getResponseBody("Big5"));//谷歌的输出编码集为Big5，反向解析结果的时候使用
            }
            long stop = System.currentTimeMillis();
            System.out.println("cost: " + (stop - start) + "ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
    }
}
