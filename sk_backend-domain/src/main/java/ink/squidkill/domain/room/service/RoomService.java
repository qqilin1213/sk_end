package ink.squidkill.domain.room.service;

import ink.squidkill.domain.game.model.valobj.StateTypeVO;
import ink.squidkill.domain.room.model.entity.RoomEntity;
import ink.squidkill.domain.room.repository.IRoomRepository;
import ink.squidkill.domain.task.repository.ITaskRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Signed;
import java.util.Map;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 16:03
 */
@Service
public class RoomService implements IRoomService{

    @Resource
    private IRoomRepository roomRepository;

    @Override
    public void insert(RoomEntity roomEntity) {
        // 初始房间默认状态为等待
        roomRepository.insert(RoomEntity.builder()
                .roomNum(roomEntity.getRoomNum())
                .roomId(roomEntity.getRoomId())
                .status(roomEntity.getStatus())
                .hasHost(roomEntity.getHasHost())
                .build());
    }

    @Override
    public void updateRoom(RoomEntity roomEntity) {
        roomRepository.updateRoom(RoomEntity.builder()
                .roomId(roomEntity.getRoomId())
                .status(roomEntity.getStatus())
                .taskId(roomEntity.getTaskId())
                .resultImg(roomEntity.getResultImg())
                .voteResult(roomEntity.getVoteResult())
                .build());
    }

    @Override
    public RoomEntity queryRoomById(String roomId) {
        return roomRepository.queryRoomById(roomId);
    }

    @Override
    public void deleteRoom(String roomId) {
        roomRepository.deleteRoom(roomId);
    }
}
