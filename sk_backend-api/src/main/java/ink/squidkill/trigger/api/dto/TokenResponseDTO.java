package ink.squidkill.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 14:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {
    /** 房间 token **/
    String roomId;
    /** 玩家名称 **/
    String playerId;
    /** 是否存在主持人 **/
    boolean  hasHost;
    /** 房间人数 **/
    Integer roomNum;
    /** 主持人ID **/
    String hostPlayerId;
    /** 房间创建者ID **/
    String creatorPlayerId;
    /** 主持人是否准备 **/
    boolean isHostReady;
    /** 房间玩家 **/
    List<Object> participants;
}
