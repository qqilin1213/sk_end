package ink.squidkill.domain.role.service;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.domain.role.model.entity.RoleEntity;

import java.util.List;
import java.util.Map;

/**
 * Description: 角色分配策略接口
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/21 17:22
 */
public interface IRoleAssignmentStrategy {
    void assignRoles(List<GamePlayerEntity> players , Map<String, RoleEntity> roleEntitiesMap ,boolean needMiddleRole);

}
