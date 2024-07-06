package ink.squidkill.domain.task.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 任务类型枚举值
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:47
 */
@Getter
@AllArgsConstructor
public enum TaskTypeVO {
    ALL(1, "全模式任务"),
    ANARCHY(2, "真格模式任务"),
    ;

    private final Integer code;
    private final String info;
}
