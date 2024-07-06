package ink.squidkill.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 任务结果
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 19:28
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    List tasks;
}
