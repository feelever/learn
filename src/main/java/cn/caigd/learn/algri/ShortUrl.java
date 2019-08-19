package cn.caigd.learn.algri;

import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigInteger;
import java.util.Map;

public class ShortUrl {
    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int BASE = ALPHABETS.length();

    public static String shortenUrl(String url) {
        long murmur32 = Hashing.murmur3_32().hashUnencodedChars(url).padToLong();
        String encoded, value;
        encoded = encode((long)Math.sqrt(murmur32));
        return encoded;

    }

    private static String encode(long oct) {
        BigInteger octLong = BigInteger.valueOf(oct);
        StringBuilder builder = new StringBuilder(6);
        while (!octLong.equals(BigInteger.ZERO)) {
            BigInteger[] divideAndReminder = octLong.divideAndRemainder(BigInteger.valueOf(BASE));
            builder.append(ALPHABETS.charAt(divideAndReminder[1].intValue()));
            octLong = divideAndReminder[0];
        }

        return builder.reverse().toString();
    }

    public static void main(String[] args) {
        Map<String, String> map = Maps.newHashMap();
        int count = 0;
        for (int i = 0; i < 1024 * 100; i++) {
            String s = "http://"+RandomStringUtils.randomAlphanumeric(40)+".com";
            String s1 = ShortUrl.shortenUrl(s);
            if (map.containsKey(s1)) {
                System.out.print(s);
                System.out.print(",");
                System.out.print(map.get(s1));
                System.out.print(",");
                System.out.print(s1);
                System.out.print(",");
                System.out.println(i);
                count++;
            } else {
                map.put(s1, s);
            }
        }
        System.out.println(count);

    }
}
