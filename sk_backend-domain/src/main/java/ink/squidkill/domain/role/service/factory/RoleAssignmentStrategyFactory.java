package ink.squidkill.domain.role.service.factory;

import ink.squidkill.domain.role.service.IRoleAssignmentStrategy;
import ink.squidkill.domain.role.service.strategy.EightPlayerStrategy;
import ink.squidkill.domain.role.service.strategy.FourPlayerStrategy;
import ink.squidkill.domain.role.service.strategy.SixPlayerStrategy;
import org.springframework.stereotype.Service;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/21 17:23
 */
@Service
public class RoleAssignmentStrategyFactory {
    public static IRoleAssignmentStrategy getStrategy(int playerCount) {
        switch (playerCount) {
            case 4:
                return new FourPlayerStrategy();
            case 6:
                return new SixPlayerStrategy();
            case 8:
                return new EightPlayerStrategy();
            default:
                throw new IllegalArgumentException("Unsupported number of players: " + playerCount);
        }
    }
}
