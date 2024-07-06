package ink.squidkill.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 14:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDTO {
    /** 房间 token **/
    String token;
    /** 适用人数 **/
    String roomNum;
    /** 创建比赛玩家名称 **/
    String playName;
    /** 是否存在主持人 */
    Boolean isNeedHost;
    /** 是否需要呆呆鱿 **/
    Boolean isNeedMiddleRole;
}
