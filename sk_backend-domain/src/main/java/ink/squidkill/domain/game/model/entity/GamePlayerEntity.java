package ink.squidkill.domain.game.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 比赛玩家实体
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayerEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 游戏房ID **/
    private String roomId;
    /** 玩家ID **/
    private String playerId;
    /** 角色名称 **/
    private String roleName;
    /** 是否存活 **/
    private Integer isAlive;
    /** 是否为主持人 **/
    private Integer isHost;
    /** 是否准备 **/
    private Integer isReady;
    /** 是否投票 **/
    private Integer isVoted;
    /** 是否第一 **/
    private Integer isTop;
    /** 是否重玩 **/
    private Integer isRestart;
    /** 是否为房间创建者 **/
    private Integer isCreator;
    /** 是否完成 **/
    private Integer isComplete;
    /** 票选的玩家 **/
    private String votedPlayerId;
    /** 队伍ID **/
    private String groupId;

}
