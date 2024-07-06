package ink.squidkill.infrastructure.persistent.po;

import lombok.Data;

import java.util.Date;

/**
 * Description: 角色表
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 10:58
 */
@Data
public class Role {
    /** 自增ID **/
    private String id;
    /** 角色ID **/
    private String roleId;
    /** 角色类型  **/
    private String roleType;
    /** 角色名称 **/
    private String roleName;
    /** 角色介绍 **/
    private String roleDesc;
    /** 创建时间 **/
    private Date createTime;
    /** 更新时间 **/
    private Date updateTime;
}
