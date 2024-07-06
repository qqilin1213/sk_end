package ink.squidkill.infrastructure.persistent.dao;

import ink.squidkill.domain.room.model.entity.RoomEntity;
import ink.squidkill.infrastructure.persistent.po.Room;
import org.apache.ibatis.annotations.Mapper;

/**
 * Description: 房间表DAO
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 11:13
 */
@Mapper
public interface IRoomDao {

    void insert(RoomEntity roomEntity);


    void updateRoom(RoomEntity roomEntity);


    Room queryRoomById(String roomId);

    void deleteRoom(String roomId);
}
