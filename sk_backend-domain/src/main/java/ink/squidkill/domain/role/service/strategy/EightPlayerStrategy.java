package ink.squidkill.domain.role.service.strategy;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.domain.role.model.entity.RoleEntity;
import ink.squidkill.domain.role.model.valobj.RoleTypeVO;
import ink.squidkill.domain.role.repository.IRoleRepository;
import ink.squidkill.domain.role.service.IRoleAssignmentStrategy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/21 22:47
 */
@Component
public class EightPlayerStrategy implements IRoleAssignmentStrategy {
    /**
     * 6好鱿 + 1 坏鱿 + 1 呆
     * @param players
     */
    @Override
    public void assignRoles(List<GamePlayerEntity> players,Map<String, RoleEntity> roleEntitiesMap , boolean needMiddleRole) {
        List<RoleEntity> roles = new ArrayList<>();
        RoleEntity badRoleEntity = roleEntitiesMap.get(RoleTypeVO.BAD.getInfo());
        RoleEntity goodRoleEntity = roleEntitiesMap.get(RoleTypeVO.GOOD.getInfo());
        RoleEntity middleRoleEntity = roleEntitiesMap.get(RoleTypeVO.MIDDLE.getInfo());
        roles.add(badRoleEntity);
        roles.add(middleRoleEntity);
        for (int i = 0; i < 6; i++) {
            roles.add(goodRoleEntity);
        }
        Collections.shuffle(roles);
        for (int i = 0; i < players.size(); i++) {
            players.get(i).setRoleName(roles.get(i).getRoleName());
            if(roles.get(i).getRoleName().equals(RoleTypeVO.BAD.getInfo())){
                players.get(i).setIsVoted(1);
            }else{
                players.get(i).setIsVoted(null);
            }
        }
    }
}
