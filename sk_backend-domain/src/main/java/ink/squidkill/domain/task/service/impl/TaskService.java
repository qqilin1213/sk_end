package ink.squidkill.domain.task.service.impl;

import ink.squidkill.domain.task.model.entity.TaskEntity;
import ink.squidkill.domain.task.model.valobj.TaskVO;
import ink.squidkill.domain.task.repository.ITaskRepository;
import ink.squidkill.domain.task.service.ITaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 19:18
 */
@Service
public class TaskService implements ITaskService {

    @Resource
    ITaskRepository taskRepository;
    @Override
    public List<TaskEntity> queryTasksByType(List<String> type) {
        return taskRepository.queryTasksByType(type);
    }

    @Override
    public TaskEntity lotteryTask(TaskVO taskVO) {
        return taskRepository.lotteryTask(taskVO);
    }

    @Override
    public TaskEntity queryTaskById(String taskId) {
        return taskRepository.queryTaskById(taskId);
    }
}
