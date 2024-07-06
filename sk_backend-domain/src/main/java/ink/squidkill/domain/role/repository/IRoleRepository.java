package ink.squidkill.domain.role.repository;

import ink.squidkill.domain.role.model.entity.RoleEntity;

import java.util.List;
import java.util.Map;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/21 17:30
 */
public interface IRoleRepository {
    List<RoleEntity> findAllRoleEntities();

    Map<String,RoleEntity> getRoleEntitiesMap();
}
