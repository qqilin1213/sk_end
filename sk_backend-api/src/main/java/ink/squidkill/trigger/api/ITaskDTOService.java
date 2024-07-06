package ink.squidkill.trigger.api;

import ink.squidkill.trigger.api.dto.RodomTaskResponseDTO;
import ink.squidkill.trigger.api.dto.TaskRequestDTO;
import ink.squidkill.trigger.api.dto.TaskResponseDTO;
import ink.squidkill.types.model.Response;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 19:33
 */
public interface ITaskDTOService {
    Response<TaskResponseDTO> getTaskList(TaskRequestDTO request);

    Response<RodomTaskResponseDTO> lotteryTask(TaskRequestDTO request);
}
