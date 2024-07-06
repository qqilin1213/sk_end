package ink.squidkill.domain.room.repository;

import ink.squidkill.domain.room.model.entity.RoomEntity;

/**
 * Description: 房间仓储服务
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 15:31
 */
public interface IRoomRepository {
    void insert(RoomEntity roomEntity);

    void updateRoom(RoomEntity roomEntity);

    RoomEntity queryRoomById(String roomId);

    void deleteRoom(String roomId);
}
