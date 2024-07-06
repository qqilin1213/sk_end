package ink.squidkill.infrastructure.persistent.dao;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.infrastructure.persistent.po.GamePlayer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description: 比赛玩家表DAO
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:12
 */
@Mapper
public interface IGamePlayerDao {
    void insert(GamePlayer entity);

    void updatePlayer(GamePlayer entity);

    void updatePlayers(GamePlayer entity);

    void updatePlayersWithIsRestart(GamePlayer entity);

    GamePlayer queryGamePlayer(GamePlayer entity);

    void deletePlayer(GamePlayer entity);

    void deletePlayerWithIsRestart(GamePlayer entity);

    List<GamePlayer> queryGamePlayers(GamePlayer entity);

    void deleteRoomPlayers(String roomId);
}
