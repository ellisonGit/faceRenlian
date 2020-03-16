package main.java.com.ha.qiniu.storage;

import main.java.com.ha.qiniu.http.Response;

/**
 * 定义了文件上传结束回调接口
 */
public interface UpCompletionHandler {
    void complete(String key, Response r);
}
