package ink.squidkill.domain.game.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

/**
 * Description: 令牌
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:53
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenVO {
    /** 房间 token **/
    String token;
    /** 适用人数 **/
    String num;
    /** 主持人 **/
    String hostName;
    /** 是否有主持人 **/
    Boolean hasHost;
}
