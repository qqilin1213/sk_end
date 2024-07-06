package ink.squidkill.infrastructure.persistent.repository;

import ink.squidkill.domain.game.model.valobj.StateTypeVO;
import ink.squidkill.domain.room.model.entity.RoomEntity;
import ink.squidkill.domain.room.repository.IRoomRepository;
import ink.squidkill.infrastructure.persistent.dao.IRoomDao;
import ink.squidkill.infrastructure.persistent.po.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 15:39
 */
@Slf4j
@Repository
public class RoomRepository implements IRoomRepository {

    @Resource
    private IRoomDao roomDao;

    @Override
    public void insert(RoomEntity roomEntity) {
        roomDao.insert(roomEntity);
    }

    @Override
    public void updateRoom(RoomEntity roomEntity) {
        roomDao.updateRoom(roomEntity);
    }

    @Override
    public RoomEntity queryRoomById(String roomId) {
        Room room = roomDao.queryRoomById(roomId);
        RoomEntity roomEntity = null;
        if(room != null){
            roomEntity = RoomEntity.builder()
                    .voteResult(room.getVoteResult())
                    .taskId(room.getTaskId())
                    .hasHost(room.getHasHost())
                    .roomId(room.getRoomId())
                    .roomNum(room.getRoomNum())
                    .status(room.getStatus())
                    .isNeedMiddle(room.getIsNeedMiddle())
                    .resultImg(room.getResultImg())
                    .build();
        }

        return roomEntity;
    }

    @Override
    public void deleteRoom(String roomId) {
        roomDao.deleteRoom(roomId);
    }
}
