package ink.squidkill.infrastructure.persistent.repository;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.domain.game.repository.IGamePlayerRepository;
import ink.squidkill.infrastructure.persistent.dao.IGamePlayerDao;
import ink.squidkill.infrastructure.persistent.po.GamePlayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 比赛过程服务
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 16:21
 */
@Slf4j
@Repository
public class GamePlayerRepository implements IGamePlayerRepository {

    @Resource
    private IGamePlayerDao gamePlayerDao;


    @Override
    public void savePlayer(GamePlayerEntity entity) {
        GamePlayer playerReq = new GamePlayer();
        playerReq.setRoomId(entity.getRoomId());
        playerReq.setPlayerId(entity.getPlayerId());
        playerReq.setIsHost(entity.getIsHost());
        playerReq.setIsAlive(entity.getIsAlive());
        playerReq.setIsReady(entity.getIsReady());
        playerReq.setIsRestart(entity.getIsRestart());
        playerReq.setIsCreator(entity.getIsCreator());
        gamePlayerDao.insert(playerReq);
    }

    @Override
    public void updatePlayer(GamePlayerEntity entity) {
        GamePlayer playerReq = new GamePlayer();
        playerReq.setRoomId(entity.getRoomId());
        playerReq.setIsComplete(entity.getIsComplete());
        playerReq.setPlayerId(entity.getPlayerId());
        playerReq.setRoleName(entity.getRoleName());
        playerReq.setGroupId(entity.getGroupId());
        playerReq.setIsAlive(entity.getIsAlive());
        playerReq.setIsReady(entity.getIsReady());
        playerReq.setIsVoted(entity.getIsVoted());
        playerReq.setVotedPlayerId(entity.getVotedPlayerId());
        playerReq.setIsTop(entity.getIsTop());
        playerReq.setIsRestart(entity.getIsRestart());
        playerReq.setIsHost(entity.getIsHost());
        gamePlayerDao.updatePlayer(playerReq);
    }

    @Override
    public void updatePlayers(GamePlayerEntity entity) {
        GamePlayer playerReq = new GamePlayer();
        playerReq.setRoomId(entity.getRoomId());
        playerReq.setIsComplete(entity.getIsComplete());
        playerReq.setRoleName(entity.getRoleName());
        playerReq.setGroupId(entity.getGroupId());
        playerReq.setIsAlive(entity.getIsAlive());
        playerReq.setIsReady(entity.getIsReady());
        playerReq.setIsVoted(entity.getIsVoted());
        playerReq.setVotedPlayerId(entity.getVotedPlayerId());
        playerReq.setIsTop(entity.getIsTop());
        playerReq.setIsRestart(entity.getIsRestart());
        gamePlayerDao.updatePlayers(playerReq);
    }

    @Override
    public void updatePlayersWithIsRestart(GamePlayerEntity entity) {
        GamePlayer playerReq = new GamePlayer();
        playerReq.setRoomId(entity.getRoomId());
        playerReq.setIsComplete(entity.getIsComplete());
        playerReq.setRoleName(entity.getRoleName());
        playerReq.setGroupId(entity.getGroupId());
        playerReq.setIsAlive(entity.getIsAlive());
        playerReq.setIsReady(entity.getIsReady());
        playerReq.setIsVoted(entity.getIsVoted());
        playerReq.setVotedPlayerId(entity.getVotedPlayerId());
        playerReq.setIsTop(entity.getIsTop());
        playerReq.setIsRestart(entity.getIsRestart());
        gamePlayerDao.updatePlayersWithIsRestart(playerReq);
    }

    @Override
    public GamePlayerEntity queryGamePlayer(String roomId, String playerId) {
        GamePlayer playerReq = new GamePlayer();
        playerReq.setRoomId(roomId);
        playerReq.setPlayerId(playerId);
        GamePlayer gamePlayer = gamePlayerDao.queryGamePlayer(playerReq);
        if(null != gamePlayer) {
            return GamePlayerEntity.builder()
                    .roomId(gamePlayer.getRoomId())
                    .playerId(gamePlayer.getPlayerId())
                    .roleName(gamePlayer.getRoleName())
                    .isAlive(gamePlayer.getIsAlive())
                    .isReady(gamePlayer.getIsReady())
                    .build();
        }
        return null;
    }

    @Override
    public void deletePlayer(String roomId,String playerId) {
        GamePlayer playerReq = new GamePlayer();
        playerReq.setRoomId(roomId);
        playerReq.setPlayerId(playerId);
        gamePlayerDao.deletePlayer(playerReq);
    }

    @Override
    public void deleteRoomByRoomId(String roomId) {
        gamePlayerDao.deleteRoomPlayers(roomId);
    }

    @Override
    public List<GamePlayerEntity> queryGamePlayers(String roomId) {
        GamePlayer playerReq = new GamePlayer();
        playerReq.setRoomId(roomId);
        List<GamePlayer> gamePlayers = gamePlayerDao.queryGamePlayers(playerReq);
        List<GamePlayerEntity> gamePlayerEntities = new ArrayList<>(gamePlayers.size());

        for(GamePlayer gamePlayer : gamePlayers){
            GamePlayerEntity gamePlayerEntity = GamePlayerEntity.builder()
                    .isVoted(gamePlayer.getIsVoted())
                    .isRestart(gamePlayer.getIsRestart())
                    .isCreator(gamePlayer.getIsCreator())
                    .roleName(gamePlayer.getRoleName())
                    .isTop(gamePlayer.getIsTop())
                    .votedPlayerId(gamePlayer.getVotedPlayerId())
                    .isReady(gamePlayer.getIsReady())
                    .isHost(gamePlayer.getIsHost())
                    .isAlive(gamePlayer.getIsAlive())
                    .isComplete(gamePlayer.getIsComplete())
                    .roomId(roomId)
                    .playerId(gamePlayer.getPlayerId())
                    .groupId(gamePlayer.getGroupId())
                    .build();
            gamePlayerEntities.add(gamePlayerEntity);
        }
        return gamePlayerEntities;
    }

    @Override
    public void deletePlayerWithIsRestart(String roomId) {
        GamePlayer playerReq = new GamePlayer();
        playerReq.setRoomId(roomId);
        gamePlayerDao.deletePlayerWithIsRestart(playerReq);
    }
}
