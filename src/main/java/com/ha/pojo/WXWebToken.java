package main.java.com.ha.pojo;

import com.google.gson.annotations.Expose;
import lombok.Data;

/**
 * Description:
 * User: Ellison
 *
 * Modified:
 */
@Data
public class WXWebToken {

    @Expose
    private String access_token;
    @Expose
    private Integer expires_in;
    @Expose
    private String refresh_token;
    @Expose
    private String openid;
    @Expose
    private String scope;
}
