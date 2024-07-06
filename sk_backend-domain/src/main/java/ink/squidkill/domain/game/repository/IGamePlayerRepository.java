package ink.squidkill.domain.game.repository;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.domain.room.model.entity.RoomEntity;

import java.util.List;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 16:19
 */
public interface IGamePlayerRepository {

    void savePlayer(GamePlayerEntity entity);

    void updatePlayer(GamePlayerEntity entity);

    void updatePlayers(GamePlayerEntity entity);

    void updatePlayersWithIsRestart(GamePlayerEntity entity);

    GamePlayerEntity queryGamePlayer(String roomId,String playerId);

    void deletePlayer(String roomId,String playerId);

    void deleteRoomByRoomId(String roomId);

    List<GamePlayerEntity> queryGamePlayers(String roomId);

    void deletePlayerWithIsRestart(String roomId);
}
