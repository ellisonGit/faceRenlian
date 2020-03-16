package main.java.com.ha.qiniu.linking;

import main.java.com.ha.qiniu.common.Constants;
import main.java.com.ha.qiniu.common.QiniuException;
import main.java.com.ha.qiniu.http.Client;
import main.java.com.ha.qiniu.http.Response;
import main.java.com.ha.qiniu.linking.model.SaveasReply;
import main.java.com.ha.qiniu.linking.model.SegmentListing;
import main.java.com.ha.qiniu.util.Auth;
import main.java.com.ha.qiniu.util.Json;
import main.java.com.ha.qiniu.util.StringMap;
import main.java.com.ha.qiniu.util.UrlSafeBase64;

public class LinkingVodManager {


    private final Auth auth;
    private final String host;
    private final Client client;

    public LinkingVodManager(Auth auth) {
        this(auth, "http://linking.qiniuapi.com");
    }

    public LinkingVodManager(Auth auth, String host) {
        this(auth, host, new Client());
    }

    public LinkingVodManager(Auth auth, String host, Client client) {
        this.auth = auth;
        this.host = host;
        this.client = client;
    }

    public SegmentListing querySegments(String appid, String deviceName, long start, long end, String marker, int limit)
            throws QiniuException {
        String encodedDeviceName = UrlSafeBase64.encodeToString(deviceName);
        StringMap map = new StringMap().putNotEmpty("marker", marker).
                putWhen("start", start, start > 0).putWhen("end", end, end > 0).
                putWhen("limit", limit, limit > 0);
        String queryString = map.formString();
        String url = String.format("%s/v1/apps/%s/devices/%s/vod/segments?%s",
                host, appid, encodedDeviceName, queryString);
        StringMap headers = auth.authorizationV2(url, "GET", null, null);
        Response res = client.get(url, headers);
        if (!res.isOK()) {
            throw new QiniuException(res);
        }
        SegmentListing ret = res.jsonToObject(SegmentListing.class);
        res.close();
        return ret;
    }

    public SaveasReply saveAs(String appid, String deviceName, long start, long end, String fname, String format)
            throws QiniuException {
        String encodedDeviceName = UrlSafeBase64.encodeToString(deviceName);
        StringMap map = new StringMap().put("start", start).put("end", end).
                putNotEmpty("fname", fname).putNotEmpty("format", format);
        String url = String.format("%s/v1/apps/%s/devices/%s/vod/saveas", host, appid, encodedDeviceName);
        byte[] body = Json.encode(map).getBytes(Constants.UTF_8);
        StringMap headers = auth.authorizationV2(url, "POST", body, Client.JsonMime);
        Response res = client.post(url, body, headers, Client.JsonMime);
        if (!res.isOK()) {
            throw new QiniuException(res);
        }
        SaveasReply ret = res.jsonToObject(SaveasReply.class);
        res.close();
        return ret;
    }
}
