package ink.squidkill.domain.room.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 状态枚举值
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:39
 */
@Getter
@AllArgsConstructor
public enum RoomStatusVO {

    WAITING("waiting", "等待"),
    ASSIGNINGROLES("assigning_roles","分配角色"),
    ASSIGNINGTASKS("assigning_tasks","分配任务"),
    STARTGAME("start_game","开始比赛"),
    DISCUSSING("discussing","开始讨论"),
    DISCUSSED("discussed","讨论结束"),
    VOTING("voting","开始投票"),
    FINISHED("finished","比赛结束"),
    RESTART("restart","重新开始")
    ;

    private final String code;
    private final String info;
}
