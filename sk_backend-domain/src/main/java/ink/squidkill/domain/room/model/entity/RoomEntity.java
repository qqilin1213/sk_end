package ink.squidkill.domain.room.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Description: 房间实体
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomEntity {
    /** 房间ID **/
    private String roomId;
    /** 任务ID **/
    private String taskId;
    /** 房间人数 **/
    private Integer roomNum;
    /** 房间状态 **/
    private String status;
    /** 是否存在主持人 **/
    private Integer hasHost;
    /** 人数为6时是否需要呆呆鱿 **/
    private Integer isNeedMiddle;
    /** 投票阶段的战绩图 **/
    private String resultImg;
    /** 投票结果 **/
    private String voteResult;
}
