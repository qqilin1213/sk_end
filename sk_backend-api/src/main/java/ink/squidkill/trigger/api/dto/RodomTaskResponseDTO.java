package ink.squidkill.trigger.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/25 17:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RodomTaskResponseDTO {
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
