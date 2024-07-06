package ink.squidkill.domain.role.service;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.domain.game.service.IGamePlayerService;
import ink.squidkill.domain.role.model.entity.RoleEntity;
import ink.squidkill.domain.role.model.valobj.RoleTypeVO;
import ink.squidkill.domain.role.repository.IRoleRepository;
import ink.squidkill.domain.role.service.factory.RoleAssignmentStrategyFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/21 22:49
 */
@Service
public class RoleService {

    @Resource
    IRoleRepository roleRepository;

    @Resource
    IGamePlayerService gamePlayerService;


    public List<GamePlayerEntity>  assignRolesAndGroups(String roomId, boolean isNeedMiddleRole) {
        final List<GamePlayerEntity> players = filterNonHosts(gamePlayerService.queryGamePlayers(roomId));
        // 过滤
        int playerCount = players.size();
        Map<String, RoleEntity> roleEntitiesMap = roleRepository.getRoleEntitiesMap();
        IRoleAssignmentStrategy strategy = RoleAssignmentStrategyFactory.getStrategy(playerCount);
        strategy.assignRoles(players,roleEntitiesMap, isNeedMiddleRole);
        assignGroups(players);
        for(GamePlayerEntity player : players) {
            gamePlayerService.updatePlayer(player);
        }
        return players;
    }

    private void assignGroups(List<GamePlayerEntity> players) {
        List<GamePlayerEntity> group1 = new ArrayList<>();
        List<GamePlayerEntity> group2 = new ArrayList<>();
        GamePlayerEntity badSquid = null;
        GamePlayerEntity dumbSquid = null;
        List<GamePlayerEntity> goodSquids = new ArrayList<>();

        for (GamePlayerEntity player : players) {
            if (player.getRoleName().equals(RoleTypeVO.BAD.getInfo())) {
                badSquid = player;
            } else if (player.getRoleName().equals(RoleTypeVO.MIDDLE.getInfo())) {
                dumbSquid = player;
            } else {
                goodSquids.add(player);
            }
        }

        Collections.shuffle(goodSquids);
        Random random = new Random();

        // Randomly assign badSquid and dumbSquid to different groups
        if (badSquid != null && dumbSquid != null) {
            if (random.nextBoolean()) {
                group1.add(badSquid);
                group2.add(dumbSquid);
            } else {
                group2.add(badSquid);
                group1.add(dumbSquid);
            }
        } else {
            if (badSquid != null) {
                if (random.nextBoolean()) {
                    group1.add(badSquid);
                } else {
                    group2.add(badSquid);
                }
            }
            if (dumbSquid != null) {
                if (random.nextBoolean()) {
                    group1.add(dumbSquid);
                } else {
                    group2.add(dumbSquid);
                }
            }
        }

        // Distribute good squids evenly
        for (int i = 0; i < goodSquids.size(); i++) {
            if (group1.size() < players.size() / 2) {
                group1.add(goodSquids.get(i));
            } else {
                group2.add(goodSquids.get(i));
            }
        }

        // 归队
        for (GamePlayerEntity player : group1) {
            player.setGroupId("A");
        }
        for (GamePlayerEntity player : group2) {
            player.setGroupId("B");
        }
    }

    private List<GamePlayerEntity> filterNonHosts(List<GamePlayerEntity> players) {
        return players.stream()
                .filter(player -> player.getIsHost() != 1)
                .collect(Collectors.toList());
    }
}
