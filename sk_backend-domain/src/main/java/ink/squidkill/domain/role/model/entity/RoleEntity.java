package ink.squidkill.domain.role.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 角色实体
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    /** 角色ID **/
    private String roleId;
    /** 角色名称 **/
    private String roleName;
    /** 角色类型 **/
    private String roleType;
    /** 角色介绍 **/
    private String roleDesc;
}
