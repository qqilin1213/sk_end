package ink.squidkill.domain.role.model.valobj;

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
public enum RoleTypeVO {

    GOOD("10000001", "好鱿"),
    BAD("10000010", "坏鱿"),
    MIDDLE("10000011", "呆呆鱿"),
    ;

    private final String code;
    private final String info;
}
