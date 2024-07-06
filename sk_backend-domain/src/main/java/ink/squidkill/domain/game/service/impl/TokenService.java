package ink.squidkill.domain.game.service.impl;

import com.google.common.cache.LoadingCache;
import ink.squidkill.domain.game.model.valobj.TokenVO;
import ink.squidkill.domain.game.service.ITokenService;
import ink.squidkill.domain.game.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: token服务实现类
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 13:44
 */
@Service
@Slf4j
public class TokenService implements ITokenService {

    private final LoadingCache<String, AtomicInteger> tokenCache;

    public TokenService(@Qualifier("tokenCache") LoadingCache<String, AtomicInteger> tokenCache) {
        this.tokenCache = tokenCache;
    }


    @Override
    public TokenVO generateToken(TokenVO tokenReq) {
        Integer roomNum = 0;
        if(!tokenReq.getHasHost()){
            roomNum = Integer.valueOf(tokenReq.getNum()) - 1;
        }else{
            roomNum = Integer.valueOf(tokenReq.getNum());
        }
        String token = TokenUtils.generateUniqueToken();
        tokenCache.put(token, new AtomicInteger(roomNum));
        TokenVO tokenRes = TokenVO.builder().num(roomNum.toString())
                .hostName(tokenReq.getHostName())
                .token(token).build();
        return tokenRes;
    }

    @Override
    public String useToken(TokenVO token) {
        try {
            // 获取缓存值
            AtomicInteger usageCount = tokenCache.getIfPresent(token.getToken());

            // 如果获取的缓存值为null，表示缓存中不存在对应的key
            if (usageCount == null) {
                // 处理缓存中不存在的情况，例如重新加载或者返回默认值
                log.warn("Token {} not found in cache. Handling missing token...", token.getToken());
                return "不存在房间";
            }

            // 减少使用计数
            int count = usageCount.decrementAndGet();

            // 如果计数大于0，则表示可以继续使用token
            if (count >= 0) {
                return "YES";
            } else {
                // 如果计数小于0，说明token已经被使用过了
                log.warn("Token {} 已经被使用过了.", token.getToken());
                return "房间人数已满";
            }

        } catch (Exception e) {
            log.error("不存在 key: {}", e.getMessage(), e);
            return "不存在房间";
        }
    }

    @Override
    public void releaseToken(TokenVO token) {
        try {
            AtomicInteger usageCount = tokenCache.get(token.getToken());
            usageCount.incrementAndGet();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成token
     * @param length 字符串长度
     * @return token字符串
     */
    private String generateUniqueToken(int length){
        return null;
    }
}
