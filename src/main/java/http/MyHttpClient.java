package http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class MyHttpClient {

    // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
    CloseableHttpClient httpClient;
    HttpGet httpGet;

    public MyHttpClient() {
        httpClient = HttpClientBuilder.create().build();
        httpGet = new HttpGet("http://localhost:8089/test");
//        httpClient.getConnectionManager().closeExpiredConnections();

//        RequestConfig requestConfig = RequestConfig.custom()
//                // 设置连接超时时间(单位毫秒)
//                .setConnectTimeout(5000)
//                // 设置请求超时时间(单位毫秒)
//                .setConnectionRequestTimeout(5000)
//                // socket读写超时时间(单位毫秒)
//                .setSocketTimeout(5000)
//                // 设置是否允许重定向(默认为true)
//                .setRedirectsEnabled(true).build();
//        httpGet.setConfig(requestConfig);
    }

    public static void main(String[] args) {
        MyHttpClient client = new MyHttpClient();

        CountDownLatch countDownLatch = new CountDownLatch(10);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        client.doGetTestOne();
                    }
                    countDownLatch.countDown();
                }
            }).start();
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
//            HttpEntity responseEntity = response.getEntity();
//            System.out.println("响应状态为:" + response.getStatusLine());
//            if (responseEntity != null) {
//                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
//                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
//            }
        } catch (Exception e) {
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
