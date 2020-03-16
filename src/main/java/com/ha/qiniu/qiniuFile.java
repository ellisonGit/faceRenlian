package main.java.com.ha.qiniu;

import main.java.com.ha.qiniu.common.QiniuException;
import main.java.com.ha.qiniu.http.Response;
import main.java.com.ha.qiniu.storage.Configuration;
import main.java.com.ha.qiniu.storage.UploadManager;
import main.java.com.ha.qiniu.util.Auth;
import main.java.com.ha.qiniu.util.StringMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Description:
 * User: Ellison
 * Date: 2019-11-22
 * Time: 9:31
 * Modified:
 */
public class qiniuFile {
    public static void main(String[] args) throws IOException {

        /*String key = "file key";
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackUrl", "http://api.example.com/qiniu/upload/callback");
        putPolicy.put("callbackBody", "key=$(key)&hash=$(etag)&bucket=$(bucket)&fsize=$(fsize)");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucket, null, expireSeconds, putPolicy);
        System.out.println(upToken);*/

        //new qiniuFile().upload();
        publicFile("123.jpg","http://q1bcd4bkc.bkt.clouddn.com");


    }

    /**基本配置-从七牛管理后台拿到*/
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "GIDGIgD5KtdnNHwlO9qe9g2ZxEE1b9iejCUyIrQa";
    String SECRET_KEY = "Gxt3JQXetSTXbx-Y8FbnoNGVid15ZhLGPTdhP1dY";
    //要上传的空间名--
    String bucketname = "ellisonqiniu";


    private static String accessKey = "GIDGIgD5KtdnNHwlO9qe9g2ZxEE1b9iejCUyIrQa"
            ;
    private static String secretKey = "Gxt3JQXetSTXbx-Y8FbnoNGVid15ZhLGPTdhP1dY";
    private static String bucket = "ellisonqiniu";
    /**
     * 获取上传凭证
     */
    public static String getUploadCredential() {
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        System.out.println(upToken);
        return upToken;
    }

    /**指定保存到七牛的文件名--同名上传会报错  {"error":"file exists"}*/
    /** {"hash":"FrQF5eX_kNsNKwgGNeJ4TbBA0Xzr","key":"aa1.jpg"} 正常返回 key为七牛空间地址 http:/xxxx.com/aa1.jpg */
    //上传文件的路径
    String FilePath ="D:\\t0102f39fc8bddd15e8.jpg";
    //上传到七牛后保存的文件名    访问为：http://oswj11a86.bkt.clouddn.com/daimo6.png
    String key = "t0102f39fc8bddd15e8.jpg";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager =new UploadManager(new Configuration());

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken(){
        StringMap putPolicy = new StringMap();
        putPolicy.put("callbackUrl", "http://api.example.com/qiniu/upload/callback");
        putPolicy.put("callbackBody", "key=$(key)&hash=$(etag)&bucket=$(bucket)&fsize=$(fsize)");
        long expireSeconds = 3600;
        String upToken = auth.uploadToken(bucketname, null, expireSeconds, putPolicy);
        System.out.println(upToken);
        return auth.uploadToken(bucketname);
    }
    public void upload(String FilePath,String key) throws IOException {
        try {
            //调用put方法上传

            Response res = uploadManager.put(FilePath, key, getUpToken());
            //打印返回的信息
            System.out.println(res.bodyString());
            System.out.println(res.statusCode);//200为上传成功
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }

    public static Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    /**
     * 公有空间返回文件URL
     * @param fileName
     * @param domainOfBucket
     * @return
     */
    public static String publicFile(String fileName,String domainOfBucket) {
        String encodedFileName=null;
        try {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String finalUrl = String.format("%s/%s", domainOfBucket,encodedFileName );
        System.out.println(finalUrl);
        return finalUrl;
    }

}
