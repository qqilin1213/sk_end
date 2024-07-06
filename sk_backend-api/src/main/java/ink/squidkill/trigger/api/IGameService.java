package ink.squidkill.trigger.api;

import ink.squidkill.trigger.api.dto.TokenRequestDTO;
import ink.squidkill.trigger.api.dto.TokenResponseDTO;
import ink.squidkill.types.model.Response;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/24 19:31
 */
public interface IGameService {
    Response<TokenResponseDTO> getToken(TokenRequestDTO request);
}
