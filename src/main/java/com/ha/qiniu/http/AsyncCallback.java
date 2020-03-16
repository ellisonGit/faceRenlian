package main.java.com.ha.qiniu.http;

/**
 * 请求处理完成的异步回调接口
 */
public interface AsyncCallback {
    void complete(Response r);
}
