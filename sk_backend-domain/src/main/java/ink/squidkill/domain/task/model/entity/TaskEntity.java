package ink.squidkill.domain.task.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 任务实体
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskEntity {
    private String taskId;
    /** 任务类型 **/
    private String type;
    /** 任务子类型 **/
    private String typeInfo;
    /** 任务子类型 **/
    private String subType;
    /** 任务子类型 **/
    private String subTypeInfo;
    /** 任务级别 **/
    private Integer level;
    /** 任务标题 **/
    private String title;
    /** 任务副标题 **/
    private String subTitle;
    /** 任务提供者姓名 **/
    private String createName;
}
