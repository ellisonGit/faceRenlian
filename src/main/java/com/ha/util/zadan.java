package main.java.com.ha.util;

/**
 * Description:
 * User: Ellison
 * Date: 2019-10-17
 * Time: 11:09
 * Modified:
 */
public class zadan {
    public static String zha() {
        long start = System.currentTimeMillis();
        long end = MyConfig.keytime;
        if (start > end) {
            return "0";
        }
        return"1";
    }

}
