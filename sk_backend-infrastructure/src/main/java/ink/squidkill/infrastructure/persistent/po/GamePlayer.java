package ink.squidkill.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * Description: 比赛玩家表
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:07
 */
@Data
public class GamePlayer {
    /** 自增ID **/
    private String id;
    /** 游戏房ID **/
    private String roomId;
    /** 玩家ID **/
    private String playerId;
    /** 角色ID **/
    private String roleName;
    /** 是否存活 **/
    private Integer isAlive;
    /** 是否投票 **/
    private Integer isVoted;
    /** 是否第一 **/
    private Integer isTop;
    /** 是否重玩 **/
    private Integer isRestart;
    /** 票选的玩家 **/
    private String votedPlayerId;
    /** 是否为主持人 **/
    private Integer isHost;
    /** 是否为房间创建者 **/
    private Integer isCreator;
    /** 是否准备 **/
    private Integer isReady;
    /** 是否完成 **/
    private Integer isComplete;
    /** 队伍ID **/
    private String groupId;
    /** 创建时间 **/
    private Date createTime;
    /** 更新时间 **/
    private Date updateTime;
}
