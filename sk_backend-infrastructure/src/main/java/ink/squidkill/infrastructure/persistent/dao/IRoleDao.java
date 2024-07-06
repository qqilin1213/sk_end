package ink.squidkill.infrastructure.persistent.dao;

import ink.squidkill.infrastructure.persistent.po.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description: 角色表DAO
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:13
 */
@Mapper
public interface IRoleDao {
    List<Role> findAllRoles();
}
