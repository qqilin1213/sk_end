package ink.squidkill.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 15:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDTO {
    /** roomId **/
    String roomId;
    /** 名称 **/
    String playerId;
}
