package ink.squidkill.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 14:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum GameCode {
    CREATE("0000", "createToken"),
    JOIN("0001", "joinRoom"),
    LEAVE("0002", "leaveRoom"),
    ;

    private String code;
    private String info;
}
