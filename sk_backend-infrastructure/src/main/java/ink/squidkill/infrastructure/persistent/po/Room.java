package ink.squidkill.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * Description: 房间表
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 10:58
 */
@Data
public class Room {
    /** 自增ID **/
    private String id;
    /** 房间ID **/
    private String roomId;
    /** 房间人数 **/
    private Integer roomNum;
    /** 创建时间 **/
    private Date startTime;
    /** 结束时间 **/
    private Date updateTime;
    /** 房间状态 **/
    private String status;
    /** 是否存在主持人 **/
    private Integer hasHost;
    /** 任务ID **/
    private String taskId;
    /** 人数为6时是否需要呆呆鱿 **/
    private Integer isNeedMiddle;
    /** 投票阶段的战绩图 **/
    private String resultImg;
    /** 投票结果 **/
    private String voteResult;
}
