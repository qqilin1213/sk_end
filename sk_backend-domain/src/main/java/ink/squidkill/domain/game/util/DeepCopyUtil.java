package ink.squidkill.domain.game.util;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;

import java.io.*;
import java.util.List;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 01:45
 */
public class DeepCopyUtil {
    // 深拷贝方法，接收一个实现了 Serializable 接口的对象
    public static <T extends Serializable> T deepCopy(List<GamePlayerEntity> object) {
        if (object == null) {
            return null;
        }
        T deepCopyObject = null;
        try {
            // 序列化对象
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();

            // 反序列化对象
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            deepCopyObject = (T) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return deepCopyObject;
    }
}
