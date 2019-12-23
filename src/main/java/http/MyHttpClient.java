package http;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

public class MyHttpClient {

    // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
    CloseableHttpClient httpClient;
    // 创建Get请求
    private HttpGet httpGet;

    public MyHttpClient() {
        httpClient = HttpClientBuilder.create().build();
        httpGet = new HttpGet("http://localhost:8089/test");
    }

    public static void main(String[] args) {
        MyHttpClient client = new MyHttpClient();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {

            client.doGetTestOne();
        }
        long stop = System.currentTimeMillis();
        System.out.println("cost: " + (stop - start) + "ms");
    }

    public void doGetTestOne() {
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Get请求
            response = httpClient.execute(httpGet);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
//            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
//                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
//                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
//                if (httpClient != null) {
//                    httpClient.close();
//                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
