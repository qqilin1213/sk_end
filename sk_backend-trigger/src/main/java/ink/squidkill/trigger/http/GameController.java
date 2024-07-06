package ink.squidkill.trigger.http;

import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.domain.game.model.valobj.TokenVO;
import ink.squidkill.domain.game.service.IGamePlayerService;
import ink.squidkill.domain.game.service.ITokenService;
import ink.squidkill.domain.room.model.entity.RoomEntity;
import ink.squidkill.domain.room.model.valobj.RoomStatusVO;
import ink.squidkill.domain.room.service.IRoomService;
import ink.squidkill.trigger.api.IGameService;
import ink.squidkill.trigger.api.dto.*;
import ink.squidkill.types.enums.GameCode;
import ink.squidkill.types.enums.ResponseCode;
import ink.squidkill.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description: 比赛请求
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 14:36
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/room/")
public class GameController implements IGameService {

    @Resource
    private ITokenService tokenService;

    @Resource
    private IRoomService roomService;

    @Resource
    private IGamePlayerService gamePlayerService;

    /**
     * 创建房间 - 获取token
     * @param request 请求对象
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @Override
    public Response<TokenResponseDTO> getToken(@RequestBody TokenRequestDTO request) {
        List<GamePlayerEntity> gamePlayerEntities = new ArrayList();

        try {
            log.info("创建房间token开始 , num:{} ", request.getRoomNum());

            // 1. 生成 token
            TokenVO tokenData = tokenService.generateToken(TokenVO.builder()
                    .num(request.getRoomNum())
                    .hostName(request.getPlayName())
                    .hasHost(request.getIsNeedHost())
                    .build());

            // 2. 创建房间
            roomService.insert(RoomEntity.builder()
                            .roomNum(Integer.valueOf(request.getRoomNum()))
                            .roomId(tokenData.getToken())
                            .hasHost(request.getIsNeedHost() ?1 : 0)
                            .isNeedMiddle(request.getIsNeedMiddleRole() ? 1 : 0)
                            .status(RoomStatusVO.WAITING.getCode())
                    .build());

            // 3. 加入比赛玩家表
            gamePlayerService.savePlayer(GamePlayerEntity.builder()
                    .isAlive(1)
                    .isReady(0)
                    .playerId(request.getPlayName())
                    .roomId(tokenData.getToken())
                    .isCreator(1)
                    .isHost(request.getIsNeedHost() ?1 : 0)
                    .build());

            boolean hasHost = request.getIsNeedHost();

            // 获取房间人数
            if(!hasHost){
                gamePlayerEntities  = gamePlayerService.queryGamePlayers(tokenData.getToken());
            }

            Response<TokenResponseDTO> response = Response.<TokenResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .result(TokenResponseDTO.builder()
                            .roomId(tokenData.getToken())
                            .playerId(request.getPlayName())
                            .roomNum(Integer.valueOf(request.getRoomNum()))
                            .hasHost(hasHost)
                            .hostPlayerId(hasHost ? request.getPlayName() : "")
                            .creatorPlayerId(request.getPlayName())
                            .isHostReady(false)
                            .participants(new ArrayList<>(gamePlayerEntities))
                            .build())

                    .gameCode(GameCode.CREATE.getCode())
                    .gameInfo(GameCode.CREATE.getInfo())
                    .build();

            log.info("创建房间token结束 , token:{} ", tokenData.getToken());

            return response;
        } catch (Exception e) {
            log.error("创建房间token，失败 num:{}", request.getRoomNum(), e);
            return Response.<TokenResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
