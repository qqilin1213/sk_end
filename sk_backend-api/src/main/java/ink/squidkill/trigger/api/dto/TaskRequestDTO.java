package ink.squidkill.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 任务请求
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 19:27
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    String types = "";
    String models ="";
    String gameTypes = "";
    String levels = "";
}
