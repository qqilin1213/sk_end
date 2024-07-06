package ink.squidkill.domain.game.service.impl;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.domain.game.model.valobj.TokenVO;
import ink.squidkill.domain.game.repository.IGamePlayerRepository;
import ink.squidkill.domain.game.service.IGamePlayerService;
import ink.squidkill.domain.room.model.entity.RoomEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 比赛服务接口
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 16:29
 */
@Service
public class GamePlayerService implements IGamePlayerService {
    @Resource
    private IGamePlayerRepository gamePlayerRepository;

    @Override
    public void savePlayer(GamePlayerEntity entity) {
        gamePlayerRepository.savePlayer(entity);
    }

    @Override
    public void updatePlayer(GamePlayerEntity entity) {
        gamePlayerRepository.updatePlayer(entity);
    }

    @Override
    public void updatePlayers(GamePlayerEntity entity) {
        gamePlayerRepository.updatePlayers(entity);
    }

    @Override
    public void updatePlayersWithIsRestart(GamePlayerEntity entity) {
        gamePlayerRepository.updatePlayersWithIsRestart(entity);
    }

    @Override
    public GamePlayerEntity queryGamePlayer(String roomId, String playerId) {
        return gamePlayerRepository.queryGamePlayer(roomId,playerId);
    }

    @Override
    public void deletePlayer(String roomId, String playerId) {
        gamePlayerRepository.deletePlayer(roomId,playerId);
    }

    @Override
    public void deleteRoomByRoomId(String roomId) {
        gamePlayerRepository.deleteRoomByRoomId(roomId);
    }

    @Override
    public List<GamePlayerEntity> queryGamePlayers(String roomId) {
        return gamePlayerRepository.queryGamePlayers(roomId);
    }

    @Override
    public void deletePlayerWithIsRestart(String roomId) {
        gamePlayerRepository.deletePlayerWithIsRestart(roomId);
    }
}
