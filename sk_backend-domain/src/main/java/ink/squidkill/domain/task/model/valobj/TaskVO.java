package ink.squidkill.domain.task.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 任务值对象
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/25 16:22
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskVO {
    // 模式类型
    String models ="";
    // 游戏类型
    String gameTypes = "";
    // 困难度
    String levels = "";
}
