package ink.squidkill.domain.task.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 任务等级枚举值
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:45
 */
@Getter
@AllArgsConstructor
public enum LevelTypeVO {

    SIMPLE(1, "简单任务"),
    COMMON(2, "普通任务"),
    HARD(3, "困难任务"),
    ;

    private final Integer code;
    private final String info;
}
