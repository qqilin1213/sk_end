package ink.squidkill.domain.task.repository;

import ink.squidkill.domain.task.model.entity.TaskEntity;
import ink.squidkill.domain.task.model.valobj.TaskVO;

import java.util.List;

/**
 * Description: 任务服务仓储接口
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 13:40
 */
public interface ITaskRepository {

    List<TaskEntity> queryTasksByType(List<String> type);

    TaskEntity lotteryTask(TaskVO taskVO);

    TaskEntity queryTaskById(String taskId);
}
