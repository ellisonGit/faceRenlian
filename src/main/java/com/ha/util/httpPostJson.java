package main.java.com.ha.util;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;

/**
 * Description:
 * User: Ellison
 * Date: 2019-09-26
 * Time: 10:12
 * Modified:
 */
public class httpPostJson {
    /**
     * post请求
     *
     * @param url
     * @param json
     * @return json object
     */
    public static String doPost(String url, String json) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String result = null;

        try {
            StringEntity s = new StringEntity(json);
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            //设置编码格式
            post.addHeader("Content-type","application/json;charset=utf-8");
            post.setHeader("Accept", "application/json");
            post.setEntity(new StringEntity(json, Charset.forName("UTF-8")));

            HttpResponse res = client.execute(post);
            result = res.toString();
            if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = res.getEntity();
                 result = EntityUtils.toString(entity);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}


