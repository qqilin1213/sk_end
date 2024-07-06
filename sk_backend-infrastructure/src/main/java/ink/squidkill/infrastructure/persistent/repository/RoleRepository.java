package ink.squidkill.infrastructure.persistent.repository;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import ink.squidkill.domain.role.model.entity.RoleEntity;
import ink.squidkill.domain.role.repository.IRoleRepository;
import ink.squidkill.infrastructure.persistent.dao.IRoleDao;
import ink.squidkill.infrastructure.persistent.po.Role;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/21 17:41
 */
@Slf4j
@Repository
public class RoleRepository implements IRoleRepository {

    private Map<String, RoleEntity> roleEntityMap = new HashMap<>();

    @Resource
    private IRoleDao roleDao;

    @Override
    public List<RoleEntity> findAllRoleEntities() {
        List<Role> roles = roleDao.findAllRoles();
        List<RoleEntity> roleEntities = new ArrayList<>(roles.size());
        for (Role role : roles) {
            RoleEntity roleEntity = RoleEntity.builder()
                    .roleId(role.getRoleId())
                    .roleName(role.getRoleName())
                    .roleDesc(role.getRoleDesc())
                    .build();
            roleEntities.add(roleEntity);
        }
        return roleEntities;
    }

    @Override
    public Map<String, RoleEntity> getRoleEntitiesMap() {
        if (roleEntityMap.isEmpty()) {
            List<RoleEntity> allRoleEntities = this.findAllRoleEntities();
            for (RoleEntity roleEntity : allRoleEntities) {
                roleEntityMap.put(roleEntity.getRoleName(), roleEntity);
            }
        }
        return roleEntityMap;
    }
}
