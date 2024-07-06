package ink.squidkill.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * Description: 任务表
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:04
 */
@Data
public class Task {
    private Long id;
    private String taskId;
    private String type;
    private String subType;
    private Integer level;
    private String title;
    private String subTitle;
    private String createName;
    private Date createTime;
    private Date updateTime;
}
