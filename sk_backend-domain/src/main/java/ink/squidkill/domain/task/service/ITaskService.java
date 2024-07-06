package ink.squidkill.domain.task.service;

import ink.squidkill.domain.task.model.entity.TaskEntity;
import ink.squidkill.domain.task.model.valobj.TaskVO;

import java.util.List;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 19:19
 */
public interface ITaskService {
    List<TaskEntity> queryTasksByType(List<String> type);

    TaskEntity lotteryTask(TaskVO taskVO);

    TaskEntity queryTaskById(String taskId);
}
