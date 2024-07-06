package ink.squidkill.infrastructure.persistent.repository;

import ink.squidkill.domain.task.model.entity.TaskEntity;
import ink.squidkill.domain.task.model.valobj.AnarchyTypeVO;
import ink.squidkill.domain.task.model.valobj.LevelTypeVO;
import ink.squidkill.domain.task.model.valobj.TaskTypeVO;
import ink.squidkill.domain.task.model.valobj.TaskVO;
import ink.squidkill.domain.task.repository.ITaskRepository;
import ink.squidkill.infrastructure.persistent.dao.ITaskDao;
import ink.squidkill.infrastructure.persistent.po.Task;
import ink.squidkill.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * Description: 任务存储服务
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 19:11
 */
@Slf4j
@Repository
public class TaskRepository implements ITaskRepository {

    @Resource
    ITaskDao taskDao;

    @Override
    public List<TaskEntity> queryTasksByType(List<String> types) {
        List<Task> tasks = taskDao.queryTasksByType(types);
        List<TaskEntity> taskEntities = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            TaskEntity taskEntity = TaskEntity.builder()
                    .taskId(task.getTaskId())
                    .subType(task.getSubType())
                    .subTypeInfo(task.getSubType() != null ? AnarchyTypeVO.valueOf(task.getSubType()).getInfo() : null)
                    .subTitle(task.getSubTitle())
                    .type(task.getType())
                    .typeInfo(TaskTypeVO.valueOf(task.getType()).getInfo())
                    .level(task.getLevel())
                    .title(task.getTitle())
                    .createName(task.getCreateName())
                    .build();

            taskEntities.add(taskEntity);
        }
        return taskEntities;
    }

    @Override
    public TaskEntity lotteryTask(TaskVO taskVO) {
        TaskEntity randomTask = null;
        List<String> types = new ArrayList<>();
        String models = taskVO.getModels();
        String gameTypes = taskVO.getGameTypes();
        String levels = taskVO.getLevels();
        if(!StringUtils.isEmpty(models)){
            types = Arrays.asList(taskVO.getModels().split(Constants.SPLIT));
        }
        List<TaskEntity> taskEntities = this.queryTasksByType(types);

        if(!StringUtils.isEmpty(gameTypes)){
            List<String> gameTypeList = Arrays.asList(gameTypes.split(Constants.SPLIT));
            taskEntities = filterTasksByGameTypes(taskEntities,gameTypeList);
        }

        if(!StringUtils.isEmpty(levels)){
            List<String> levelList = Arrays.asList(levels.split(Constants.SPLIT));
            taskEntities = filterTasksByLevels(taskEntities,levelList);
        }

        Collections.shuffle(taskEntities);
        // 随机获取一个任务
        if (!taskEntities.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(taskEntities.size());
            randomTask = taskEntities.get(randomIndex);

            // 打印随机获取的任务
            log.info("随机任务: " + randomTask.getTitle() + " - " + randomTask.getSubType() + " - " + randomTask.getLevel());
        } else {
            log.info("没有符合条件的任务。");
        }

        return randomTask;
    }

    @Override
    public TaskEntity queryTaskById(String taskId) {
        List<Task> tasks = taskDao.queryTaskById(taskId);

        if(tasks != null && tasks.size() > 0) {
            Task task = tasks.get(0);
            return TaskEntity.builder()
                    .subType(task.getSubType())
                    .subTypeInfo(task.getSubType() != null ? AnarchyTypeVO.valueOf(task.getSubType()).getInfo() : null)
                    .subTitle(task.getSubTitle())
                    .type(task.getType())
                    .typeInfo(TaskTypeVO.valueOf(task.getType()).getInfo())
                    .level(task.getLevel())
                    .title(task.getTitle())
                    .createName(task.getCreateName())
                    .build();
        }else {
            return null;
        }
    }

    private List<TaskEntity> filterTasksByGameTypes(List<TaskEntity> tasks, List<String> gameTypes) {
        List<TaskEntity> filteredTasks = new ArrayList<>();

        for (TaskEntity task : tasks) {
            if (gameTypes.contains(task.getSubType())) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    private List<TaskEntity> filterTasksByLevels(List<TaskEntity> tasks, List<String> levels) {
        List<TaskEntity> filteredTasks = new ArrayList<>();

        for (TaskEntity task : tasks) {
            if (levels.contains(task.getLevel().toString())) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }
}
