package bs.common;

import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class ComHttp {
    //请求
    public static String request(String url, String method, String params) {
        HttpUriRequestBase http;
        switch (method) {
            case "post":
                http = new HttpPost(url);
                break;
            case "put":
                http = new HttpPut(url);
                break;
            case "delete":
                http = new HttpDelete(url);
                break;
            default:
                http = new HttpGet(url);
                break;
        }
        return request(http, params);
    }

    //请求地址
    static String request(HttpUriRequestBase http) {
        return request(http, "");
    }

    //请求地址
    static String request(HttpUriRequestBase http, String params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        http.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
        String result = "";
        try {
            CloseableHttpResponse response = httpClient.execute(http);
            if (response.getCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //get请求
    public static String get(String url) {
        return request(new HttpGet(url));
    }

    //post请求
    public static String post(String url, String params) {
        return request(new HttpPost(url));
    }
}
