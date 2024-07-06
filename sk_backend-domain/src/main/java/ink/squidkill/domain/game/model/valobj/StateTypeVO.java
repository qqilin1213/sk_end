package ink.squidkill.domain.game.model.valobj;

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
public enum StateTypeVO {

    WATTING("1", "等待"),
    IN_PROCESS("2", "进行中"),
    COMPLETED("3", "结束"),
    ;

    private final String code;
    private final String info;
}
