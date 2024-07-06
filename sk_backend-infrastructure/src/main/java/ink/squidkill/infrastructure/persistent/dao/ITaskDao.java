package ink.squidkill.infrastructure.persistent.dao;

import ink.squidkill.infrastructure.persistent.po.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 任务表DAO
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:14
 */
@Mapper
public interface ITaskDao {
    List<Task> queryTasksByType(@Param("types") List<String> types);

    List<Task> queryTaskById(String taskId);
}
