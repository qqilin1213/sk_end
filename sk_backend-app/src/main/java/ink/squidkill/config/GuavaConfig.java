package ink.squidkill.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class GuavaConfig {

    @Bean(name = "tokenCache")
    public LoadingCache<String, AtomicInteger> tokenCache() {

        LoadingCache<String, AtomicInteger> tokenCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<String, AtomicInteger>() {
                    @Override
                    public AtomicInteger load(String key) {
                        // 默认情况下，返回一个初始值为0的AtomicInteger
                        return null; // 或者根据需求返回其他值
                    }
                });
        return tokenCache;
    }

}
