package ink.squidkill.domain.task.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 真格类型
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 19:02
 */
@Getter
@AllArgsConstructor
public enum AnarchyTypeVO {
    AREA("AREA", "区域模式"),
    YUHU("YUHU", "鱼虎模式"),
    TA("TA", "占塔模式"),
    GELI("GELI", "蛤蜊模式"),
    ALL("ALL", "全模式"),
    ;

    private final String code;
    private final String info;
}
