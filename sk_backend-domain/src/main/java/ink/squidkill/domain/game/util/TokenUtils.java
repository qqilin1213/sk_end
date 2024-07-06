package ink.squidkill.domain.game.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/20 10:52
 */
public class TokenUtils {
    private static final int TOKEN_LENGTH = 6;
    private static final char[] CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int EXPECTED_INSERTIONS = 10000; // 预期的插入数量
    private static final double FPP = 0.01; // 允许的假阳性率

    private static BloomFilter<String> bloomFilter = BloomFilter.create(
            Funnels.stringFunnel(StandardCharsets.UTF_8), EXPECTED_INSERTIONS, FPP);

    private TokenUtils() {
        // 私有构造函数以防止实例化
    }

    public static String generateUniqueToken() {
        String token;
        do {
            token = generateToken();
        } while (bloomFilter.mightContain(token));

        bloomFilter.put(token);
        return token;
    }

    private static String generateToken() {
        StringBuilder tokenBuilder = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            tokenBuilder.append(CHAR_POOL[RANDOM.nextInt(CHAR_POOL.length)]);
        }
        return tokenBuilder.toString();
    }
}
