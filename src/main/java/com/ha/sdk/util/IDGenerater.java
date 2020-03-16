package main.java.com.ha.sdk.util;

import java.util.UUID;

public class IDGenerater {

    private static String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static int scale = 62;

    private static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        int remainder = 0;

        if(num < 0) {
        	num &= 0b0111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111_1111l;
        	sb.append('1');
        } else {
        	sb.append('0');
        }        
        while (num > scale - 1) {
            /**
             * 对 scale 进行求余，然后将余数追加至 sb 中，由于是从末位开始追加的，因此最后需要反转（reverse）字符串
             */
            remainder = Long.valueOf(num % scale).intValue();
            sb.append(chars.charAt(remainder));

            num = num / scale;
        }

        sb.append(chars.charAt(Long.valueOf(num).intValue()));
        return sb.reverse().toString();
    }
    
    private static long decode(String str) {
        long num = 0;
        int index = 0;
        for (int i = 0; i < str.length()-1; i++) {
            index = chars.indexOf(str.charAt(i));
            num += (long) (index * (Math.pow(scale, str.length() - i - 1 - 1)));
        }
        
        if(str.charAt(str.length() - 1) == '1')
        	num |= 0b1000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000l;

        return num;
    }
    
    /**
	 * 从uuid的输出字符串生成短号
	 * <br>
	 * <b>注意，当前步骤不可逆！</b>
	 * 
	 * @param uuidStr 必须是UUID.randomUUID().toString()产生的
	 * @return 一个重复概率极低的19位字符串
	 */
    public static String uuid2Short(String uuidStr) {
    	UUID uuid = UUID.fromString(uuidStr);
    	return (encode(uuid.getMostSignificantBits()) + '_' + encode(uuid.getLeastSignificantBits())).substring(0, 19);
    }
    
    /**
     * 从短号解析出部分uuid字符串
     * <br>
     * <b>只能解析出前半部分原始信息，您可以使用数据库like操作去匹配</b>
     * 
     * @param shortStr 短号
     * @return 原始uuid字符串的前半部分
     */
    public static String extractUUID(String shortStr) {
    	String[] shortStrs = shortStr.split("\\_");
    	return new UUID(decode(shortStrs[0]), 0).toString().substring(0, 18);
    }
    
    /**
     * 产生一个短号
     * <br>
     * 内部使用UUID的128位随机数，但只使用了大约前面3/4的bit数据
     * 
     * @return 短号
     */
    public static String generateShortID() {
    	UUID uuid = UUID.randomUUID();
    	return (encode(uuid.getMostSignificantBits()) + '_' + encode(uuid.getLeastSignificantBits())).substring(0, 19);
    }
    
    public static void main(String[] args) {
    	for(int i =0; i < 100; ++i) {
			UUID uuid = UUID.randomUUID();
			System.out.println(uuid.toString());
			String shortStr = uuid2Short(uuid.toString());
			System.out.println(shortStr);
			System.out.println(extractUUID(shortStr));
			System.out.println();
    	}
	}
}