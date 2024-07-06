package ink.squidkill.domain.room.service;

import ink.squidkill.domain.room.model.entity.RoomEntity;

import java.util.Map;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 16:02
 */
public interface IRoomService {
    void insert(RoomEntity roomEntity);


    void updateRoom(RoomEntity roomEntity);


    RoomEntity queryRoomById(String roomId);

    void deleteRoom(String roomId);

}
