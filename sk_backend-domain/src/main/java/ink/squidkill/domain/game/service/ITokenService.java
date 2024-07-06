package ink.squidkill.domain.game.service;

import ink.squidkill.domain.game.model.valobj.TokenVO;

import java.util.concurrent.ExecutionException;

/**
 * Description: token服务接口
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 13:46
 */
public interface ITokenService {
    /**
     * 生成token
     * @param tokenReq token设置
     * @return
     */
    TokenVO generateToken(TokenVO tokenReq);

    /**
     * 使用token
     * @param token token
     * @return
     */
    String useToken(TokenVO token) throws ExecutionException;

    /**
     * 释放token
     * @param token token
     */
    void releaseToken(TokenVO token);
}
