package main.java.com.ha.qiniu.storage;

import main.java.com.ha.qiniu.common.Constants;
import main.java.com.ha.qiniu.common.QiniuException;
import main.java.com.ha.qiniu.util.Json;
import main.java.com.ha.qiniu.util.UrlSafeBase64;

/**
 * 封装了 accessKey 和 bucket 的类
 */
class RegionReqInfo {
    private final String accessKey;
    private final String bucket;

    RegionReqInfo(String token) throws QiniuException {
        // http://developer.qiniu.com/article/developer/security/upload-token.html
        // http://developer.qiniu.com/article/developer/security/put-policy.html
        try {
            String[] strings = token.split(":");
            accessKey = strings[0];
            String policy = new String(UrlSafeBase64.decode(strings[2]), Constants.UTF_8);
            bucket = Json.decode(policy).get("scope").toString().split(":")[0];
        } catch (Exception e) {
            throw new QiniuException(e, "token is invalid");
        }
    }

    RegionReqInfo(String accessKey, String bucket) {
        this.accessKey = accessKey;
        this.bucket = bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getBucket() {
        return bucket;
    }

    @Override
    public int hashCode() {
        return accessKey.hashCode() + bucket.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof RegionReqInfo) {
            RegionReqInfo t = (RegionReqInfo) obj;
            return this.accessKey.equals(t.accessKey) && this.bucket.equals(t.bucket);
        }
        return false;
    }
}
