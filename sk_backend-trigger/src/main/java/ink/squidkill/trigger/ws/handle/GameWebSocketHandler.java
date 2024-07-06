package ink.squidkill.trigger.ws.handle;
/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/19 15:18
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import ink.squidkill.domain.game.model.entity.GamePlayerEntity;
import ink.squidkill.domain.game.model.valobj.TokenVO;
import ink.squidkill.domain.game.service.IGamePlayerService;
import ink.squidkill.domain.game.service.ITokenService;
import ink.squidkill.domain.game.util.DeepCopyUtil;
import ink.squidkill.domain.role.model.valobj.RoleTypeVO;
import ink.squidkill.domain.role.service.RoleService;
import ink.squidkill.domain.room.model.entity.RoomEntity;
import ink.squidkill.domain.room.model.valobj.RoomStatusVO;
import ink.squidkill.domain.room.service.IRoomService;
import ink.squidkill.domain.task.model.entity.TaskEntity;
import ink.squidkill.domain.task.model.valobj.TaskVO;
import ink.squidkill.domain.task.service.ITaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GameWebSocketHandler extends TextWebSocketHandler {
    @Resource
    private ITokenService tokenService;
    @Resource
    private IGamePlayerService gamePlayerService;
    @Resource
    private IRoomService roomService;
    @Resource
    private ITaskService taskService;
    @Resource
    private RoleService roleService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    public void sendImage(String roomId, String imgData) throws Exception {
        this.sendImageDataAsync("setImageData", roomId, imgData);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String roomId = session.getAttributes().get("roomId").toString();
        String playerId = session.getAttributes().get("playerId").toString();
        boolean reconnect = (boolean) session.getAttributes().getOrDefault("reconnect",false);
        log.info("建立连接： roomId : {} , playerId : {}", roomId, playerId);
        roomSessions.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(session);
        // 发送当前房间状态
        if(reconnect){
            // 重连时 查询是否存在用户
            GamePlayerEntity gamePlayerEntity = gamePlayerService.queryGamePlayer(roomId, playerId);
            if(gamePlayerEntity != null){
                if(gamePlayerEntity.getIsHost() != null && gamePlayerEntity.getIsHost() == 1){
                    session.getAttributes().put("isHost",true);
                }
            }
            sendReconnectDataInfo(session);
        }
    }

    private void sendReconnectDataInfo(WebSocketSession session) {
        String roomId = session.getAttributes().get("roomId").toString();
        String playerId = session.getAttributes().get("playerId").toString();
        RoomEntity roomEntity = roomService.queryRoomById(roomId);
        Map<String, Object> message = new HashMap<>();
        if(roomEntity != null){
            List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
            GamePlayerEntity currentPlayer = gamePlayerEntities.stream().filter(gamePlayerEntity -> gamePlayerEntity.getPlayerId().equals(playerId))
                    .findFirst()
                    .orElse(null);
            message.put("event", "reconnect");
            message.put("roomStatus", roomEntity.getStatus());
            message.put("roomId", roomId);
            message.put("playerId", playerId);
            message.put("roomNum", roomEntity.getRoomNum());
            message.put("participants", gamePlayerEntities);
            message.put("isNeedMiddleRole", roomEntity.getIsNeedMiddle() != null && roomEntity.getIsNeedMiddle() == 1 ? true : false);
            // 等待阶段
            GamePlayerEntity hostPlayer = gamePlayerEntities.stream().filter(gamePlayerEntity -> gamePlayerEntity.getIsHost() != null && gamePlayerEntity.getIsHost() == 1)
                    .findFirst()
                    .orElse(null);

            GamePlayerEntity creatorPlayer = gamePlayerEntities.stream().filter(gamePlayerEntity -> gamePlayerEntity.getIsCreator() != null && gamePlayerEntity.getIsCreator() == 1)
                    .findFirst()
                    .orElse(null);

            if(creatorPlayer != null){
                message.put("creatorPlayerId",creatorPlayer.getPlayerId());
            }
            if(currentPlayer != null){
                message.put("isReady",currentPlayer.getIsReady());
                message.put("isVote",currentPlayer.getIsVoted() != null && currentPlayer.getIsVoted() == 1 ? true :false);
                message.put("isComplete",currentPlayer.getIsComplete() != null && currentPlayer.getIsCreator() == 1 ? true : false);
                message.put("isTop",currentPlayer.getIsTop() != null && currentPlayer.getIsTop() == 1 ? true : false);
            }
            if(hostPlayer != null){
                message.put("hasHost",true);
                message.put("hostPlayerId",hostPlayer.getPlayerId());
                message.put("isHostReady",hostPlayer.getIsReady() == 1 ? true : false);
            }else{
                message.put("hasHost",false);
                message.put("hostPlayerId","");
                message.put("isHostReady",false);
            }

            // 分配角色阶段/任务/投票 阶段
            if(! RoomStatusVO.WAITING.getCode().equals(roomEntity.getStatus()) && !RoomStatusVO.FINISHED.getCode().equals(roomEntity.getStatus())){
                // 执行深拷贝
                List<GamePlayerEntity> copiedPlayers  = getPlayerByplayerId(gamePlayerEntities,playerId);
                message.put("participants", copiedPlayers);

                if( currentPlayer.getIsVoted() != null){
                    message.put("participants", gamePlayerEntities);
                }

                if(roomEntity.getTaskId() != null){
                    TaskEntity taskEntity = taskService.queryTaskById(roomEntity.getTaskId());

                    GamePlayerEntity badPlayer = gamePlayerEntities.stream().filter(gamePlayerEntity -> RoleTypeVO.BAD.getInfo().equals(gamePlayerEntity.getRoleName()))
                            .findFirst()
                            .orElse(null);

                    if(badPlayer != null){
                        String badPlayerId = badPlayer.getPlayerId();
                        if(playerId.equals(badPlayerId)){
                            message.put("task", taskEntity);
                        }else{
                            message.put("task",taskEntity.builder()
                                    .subTypeInfo(taskEntity.getSubTypeInfo())
                                    .typeInfo(taskEntity.getTypeInfo())
                                    .build());
                        }
                    }
                }

                if(roomEntity.getResultImg() != null){
                    message.put("resultImgData", roomEntity.getResultImg());
                }

                if(roomEntity.getVoteResult() != null){
                    message.put("voteResult", roomEntity.getVoteResult());
                }

            }
            else if(RoomStatusVO.FINISHED.getCode().equals(roomEntity.getStatus())){
                TaskEntity taskEntity = taskService.queryTaskById(roomEntity.getTaskId());
                String voteResult = roomEntity.getVoteResult();
                boolean isComplete = gamePlayerEntities.stream().anyMatch(player ->
                        Objects.nonNull(player.getIsComplete()) && player.getIsComplete() == 1);
               String resultMsg = getGameResultInfo(voteResult,gamePlayerEntities,isComplete);
                message.put("voteResult", voteResult);
                message.put("resultMsg", resultMsg);
                message.put("isComplete", isComplete);
                message.put("task", taskEntity);
                message.put("participants", gamePlayerEntities);
            }
        }
        else{
            message.put("event","reconnect");
            message.put("info","房间不存在");
        }

        try{
            String messageJson = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(messageJson));
        }catch (Exception e){
            log.error("Error sendReconnectDataInfo :",e.getMessage());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            Map<String, Object> msg = parseMessage(payload);
            String event = msg.get("event").toString();
            // 加入房间
            if ("join".equals(event)) {
                String roomId = msg.get("roomId").toString();
                String playerId = msg.get("playerId").toString();
                Map<String, Object> resultMap = joinRoom(GamePlayerEntity.builder()
                        .roomId(roomId)
                        .playerId(playerId)
                        .build());
                String code = resultMap.getOrDefault("code", "error").toString();
                String info = resultMap.getOrDefault("info", "").toString();
                if ("success".equals(code)) {
                    sendJoinData("joinedSuccess", roomId, playerId, info);
                } else {
                    sendJoinData("joinedError", roomId, playerId, info);
                }
            }
            // 玩家准备
            else if ("ready".equals(event)) {
                String roomId = msg.get("roomId").toString();
                String playerId = msg.get("playerId").toString();
                String isReady = msg.get("isReady").toString();
                updatePlayer(GamePlayerEntity.builder()
                        .playerId(playerId)
                        .roomId(roomId)
                        .isReady(Integer.valueOf(isReady))
                        .build());
                sendReadyDataAsync("changePlayerState", roomId, playerId);
            }
            // 主持人 开始游戏
            else if ("assignRole".equals(event)) {
                String roomId = msg.get("roomId").toString();
                boolean isNeedMiddleRole = (boolean) msg.get("isNeedMiddleRole");
                // 更新房间状态
                setRoomStatus(roomId, RoomStatusVO.ASSIGNINGROLES.getCode());
                // isRestart 状态 清除
                gamePlayerService.updatePlayers(GamePlayerEntity.builder()
                        .roomId(roomId)
                        .isRestart(null)
                        .build());
                List<GamePlayerEntity> gamePlayerEntities = roleService.assignRolesAndGroups(roomId, isNeedMiddleRole);
                // 发送给 主持人
                sendRoleData("allPlayerRoles", gamePlayerEntities);
                // 发送给 玩家
                sendRoleData("playerRole", gamePlayerEntities);
            }
            // 主持人 开始游戏
            else if ("changeRoomStatus".equals(event)) {
                String roomId = msg.get("roomId").toString();
                String status = msg.get("status").toString();
                // 更新房间状态
                setRoomStatus(roomId, status);
                // 发送给 全部人
                sendInfoDataAsync("changeRoomStatus", roomId, status);
            }
            // 分配随机任务
            else if ("randomTask".equals(event)) {
                String roomId = msg.get("roomId").toString();
                // 更新房间状态
                setRoomStatus(roomId, RoomStatusVO.ASSIGNINGTASKS.getCode());
                String badPlayerId = "";
                String models = msg.get("models").toString();
                String gameTypes = msg.get("gameTypes").toString();
                String levels = msg.get("levels").toString();
                // 获取全部角色
                List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
                GamePlayerEntity badPlayer = gamePlayerEntities.stream().filter(player -> RoleTypeVO.BAD.getInfo().equals(player.getRoleName()))
                        .findFirst()
                        .orElse(null);
                if (badPlayer != null) {
                    badPlayerId = badPlayer.getPlayerId();
                }
                // 随机任务
                final TaskEntity taskEntity = taskService.lotteryTask(TaskVO.builder()
                        .models(models)
                        .gameTypes(gameTypes)
                        .levels(levels).build());
                // 存储
                roomService.updateRoom(RoomEntity.builder()
                        .roomId(roomId)
                        .taskId(taskEntity.getTaskId())
                        .build());
                // 发送给 主持人,坏鱿
                sendTaskData("randomTask", roomId, badPlayerId, taskEntity, true);
            }
            // 主持人选择任务
            else if ("setTask".equals(event)) {
                String roomId = msg.get("roomId").toString();
                // 更新房间状态
                setRoomStatus(roomId, RoomStatusVO.ASSIGNINGTASKS.getCode());
                String badPlayerId = "";
                String taskId = msg.get("taskId").toString();
                String type = msg.get("type").toString();
                String typeInfo = msg.get("typeInfo").toString();
                String subType = msg.get("subType").toString();
                String subTypeInfo = msg.get("subTypeInfo").toString();
                Integer level = Integer.valueOf(msg.get("level").toString());
                String title = msg.get("title").toString();
                String subTitle = msg.get("subTitle").toString();
                String createName = msg.get("createName").toString();
                // 获取全部角色
                List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
                GamePlayerEntity badPlayer = gamePlayerEntities.stream().filter(player -> player.getRoleName().equals(RoleTypeVO.BAD.getInfo()))
                        .findFirst()
                        .orElse(null);
                if (badPlayer != null) {
                    badPlayerId = badPlayer.getPlayerId();
                }
                // 随机任务
                TaskEntity taskEntity = TaskEntity.builder()
                        .type(type)
                        .typeInfo(typeInfo)
                        .subType(subType)
                        .subTypeInfo(subTypeInfo)
                        .level(level)
                        .title(title)
                        .subTitle(subTitle)
                        .createName(createName)
                        .build();
                // 存储
                if (!StringUtils.isEmpty(taskId)) {
                    roomService.updateRoom(RoomEntity.builder()
                            .roomId(roomId)
                            .taskId(taskId)
                            .build());
                }
                // 发送给 坏鱿
                sendTaskData("setTask", roomId, badPlayerId, taskEntity, false);
            }
            // 玩家投票
            else if ("voted".equals(event)) {
                String roomId = msg.get("roomId").toString();
                // 更新房间状态
                setRoomStatus(roomId, RoomStatusVO.VOTING.getCode());
                String playerId = msg.get("playerId").toString();
                String votedPlayerId = msg.get("votedPlayerId").toString();
                boolean isTop = (boolean) msg.get("isTop");
                updatePlayer(GamePlayerEntity.builder()
                        .playerId(playerId)
                        .roomId(roomId)
                        .isVoted(1)
                        .votedPlayerId(votedPlayerId)
                        .isTop(isTop ? 1 : 0)
                        .build());
                sendVotedResultDataAsync("showVotedResult", roomId, playerId);
            }
            // 选择任务是否完成
            else if ("taskStatus".equals(event)) {
                String roomId = msg.get("roomId").toString();
                String playerId = msg.get("playerId").toString();
                boolean isComplete = (boolean) msg.get("isComplete");
                // 更新玩家状态
                gamePlayerService.updatePlayer(GamePlayerEntity.builder()
                        .roomId(roomId)
                        .playerId(playerId)
                        .isComplete(isComplete ? 1 : 0)
                        .build());

                sendTaskStatusAsync("taskStatus", roomId, isComplete);
            }
            // 公布结果
            else if ("showResult".equals(event)) {
                String roomId = msg.get("roomId").toString();
                setRoomStatus(roomId, RoomStatusVO.FINISHED.getCode());
                boolean isComplete = (boolean) msg.get("isComplete");
                String voteResult = (String) msg.get("voteResult");

                // 更新房间
                roomService.updateRoom(RoomEntity.builder()
                                .roomId(roomId)
                                .voteResult((String) msg.get("voteResult"))
                        .build());
                sendResultDataAysnc("showResult", roomId, isComplete, voteResult);
            }
            // 离开房间
            else if ("leave".equals(event)) {
                String roomId = msg.get("roomId").toString();
                String playerId = msg.get("playerId").toString();
                removePlayer(GamePlayerEntity.builder()
                        .playerId(playerId)
                        .roomId(roomId)
                        .build());
                sendLeaveDataAsync("playerLeft", roomId, playerId);
            }
            // 玩家游戏结束离开房间
            else if ("leaveGame".equals(event)) {
                String roomId = msg.get("roomId").toString();
                String playerId = msg.get("playerId").toString();
                removePlayer(GamePlayerEntity.builder()
                        .playerId(playerId)
                        .roomId(roomId)
                        .build());
                sendGameInfoAysnc("leaveGame", roomId, playerId);
            }
            // 玩家重新开始游戏
            else if ("restartGame".equals(event)) {
                String roomId = msg.get("roomId").toString();
                String playerId = msg.get("playerId").toString();
                boolean isHost = (boolean) msg.get("isHost");
                boolean isCreator = (boolean) msg.get("isCreator");
                // 将房间中 不是重玩的 player 删除
                gamePlayerService.deletePlayerWithIsRestart(roomId);
                gamePlayerService.savePlayer(GamePlayerEntity.builder().isReady(0)
                        .isAlive(1)
                        .playerId(playerId)
                        .isCreator(isCreator ? 1 : 0)
                        .isHost(isHost ? 1 : 0)
                        .roomId(roomId)
                        .isRestart(1)
                        .build());
                sendGameInfoAysnc("restartGame", roomId, playerId);
            }
            // 游戏结束
            else if ("closeGame".equals(event)) {
                String roomId = msg.get("roomId").toString();
                gamePlayerService.deleteRoomByRoomId(roomId);
                roomService.deleteRoom(roomId);
                sendGameInfoAysnc("closeGame", roomId, null);
            }
            // 换房主
            else if ("changeHost".equals(event)) {
                String roomId = msg.get("roomId").toString();
                String oldHostId = msg.get("oldHostId").toString();
                String newHostId = msg.get("newHostId").toString();
                gamePlayerService.updatePlayer(GamePlayerEntity
                        .builder()
                        .roomId(roomId)
                        .playerId(oldHostId)
                        .isHost(0)
                        .build());
                gamePlayerService.updatePlayer(GamePlayerEntity
                        .builder()
                        .roomId(roomId)
                        .playerId(newHostId)
                        .isHost(1)
                        .build());
                sendChangeInfoAsync(event, roomId, newHostId);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 更新房间状态
     * @param roomId
     * @param status
     */
    private void setRoomStatus(String roomId,String status){
        roomService.updateRoom(RoomEntity.builder()
                .roomId(roomId)
                .status(status)
                .build());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = session.getAttributes().get("roomId").toString();
        String playerId = session.getAttributes().get("playerId").toString();
        log.info("断开连接： roomId : {} , playerId : {}", roomId, playerId);
        Set<WebSocketSession> webSocketSessions = roomSessions.get(roomId);
        if (webSocketSessions != null && webSocketSessions.size() > 0) {
            webSocketSessions.remove(session);
            if (roomSessions.get(roomId).size() == 0) {
                roomSessions.remove(roomId);
            }
        }
    }

    private Map<String, Object> parseMessage(String message) {
        Map<String, Object> map = (Map<String, Object>) JSON.parse(message);
        return map;
    }

    private void sendChangeInfoAsync(String event, String roomId, String newHostId) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
        GamePlayerEntity hostPlayer = gamePlayerService.queryGamePlayer(roomId, newHostId);
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        for (WebSocketSession session : socketSessions) {
            Map<String, Object> sessionAttributes = session.getAttributes();
            String sessionPlayerId = (String) sessionAttributes.get("playerId");
            if (sessionAttributes.get("isHost") != null) {
                sessionAttributes.remove("isHost");
            } else if (newHostId.equals(sessionPlayerId)) {
                sessionAttributes.put("isHost", true);
            }
        }
        message.put("participants", gamePlayerEntities);
        message.put("hostPlayerId", newHostId);
        message.put("isHostReady", hostPlayer.getIsReady() != null && hostPlayer.getIsReady() == 1 ? true : false);
        for (WebSocketSession session : socketSessions) {
            threadPoolExecutor.execute(() -> {
                try {
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("sendChangeInfoAsync error sending message: " + e.getMessage());
                }
            });
        }
    }

    private void sendTaskStatusAsync(String event, String roomId, boolean isCompleted) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        message.put("isComplete", isCompleted);
        for (WebSocketSession session : socketSessions) {
            threadPoolExecutor.execute(() -> {
                try {
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("sendTaskStatusAsync error sending message: " + e.getMessage());
                }
            });
        }
    }

    private void sendVotedResultDataAsync(String event, String roomId, String playerId) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        message.put("playerId", playerId);
        List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
        message.put("participants", gamePlayerEntities);
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        for (WebSocketSession session : socketSessions) {
            threadPoolExecutor.execute(() -> {
                try {
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("error sending message: " + e.getMessage());
                }
            });
        }
    }

    private void sendResultDataAysnc(String event, String roomId, boolean isComplete, String voteResultStr) throws IOException {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        RoomEntity roomEntity = roomService.queryRoomById(roomId);
        TaskEntity taskEntity = taskService.queryTaskById(roomEntity.getTaskId());
        List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);

        String resultMsg = getGameResultInfo(voteResultStr,gamePlayerEntities,isComplete);
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        message.put("resultMsg", resultMsg);
        message.put("isComplete", isComplete);
        message.put("task", taskEntity);
        message.put("participants", gamePlayerEntities);
        for (WebSocketSession session : socketSessions) {
            threadPoolExecutor.execute(() -> {
                try {
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("sendResultData error sending message: " + e.getMessage());
                }
            });
        }
    }

    private String getGameResultInfo(String voteResultStr,List<GamePlayerEntity> gamePlayerEntities,boolean isComplete){
        String resultMsg = "";
        JSONArray voteResult = JSONArray.parseArray(voteResultStr);

        JSONObject maxObject = voteResult.stream()
                .map(obj -> (JSONObject) obj)
                .max((o1, o2) -> Double.compare(o1.getDoubleValue("realValue"), o2.getDoubleValue("realValue")))
                .orElse(null);
        if (maxObject != null) {
            String playerId = (String) maxObject.get("playerId");
            String matchingRoles = gamePlayerEntities.stream()
                    .filter(player -> player.getPlayerId().equals(playerId))
                    .map(GamePlayerEntity::getRoleName) // 假设我们要连接的是 name 属性
                    .collect(Collectors.joining(","));
            resultMsg = getResultMsg(matchingRoles, isComplete);
        }
        return  resultMsg;
    }

    private String getResultMsg(String matchingRoles, boolean isComplete) {
        String resultMsg = "";
        if (!StringUtils.isEmpty(matchingRoles)) {
            if (matchingRoles.contains(RoleTypeVO.MIDDLE.getInfo())) {
                resultMsg = "呆呆鱿获胜";
            } else if (matchingRoles.contains(RoleTypeVO.BAD.getInfo())) {
                resultMsg = "好鱿获胜";
            } else if (!matchingRoles.contains(RoleTypeVO.BAD.getInfo()) && isComplete) {
                resultMsg = "坏鱿获胜";
            } else {
                resultMsg = "好鱿获胜";
            }
        }
        return resultMsg;
    }

    private void sendImageDataAsync(String event, String roomId, String imageData) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        message.put("data", imageData);
        message.put("status", "speck");
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        for (WebSocketSession session : socketSessions) {
            threadPoolExecutor.execute(() -> {
                try {
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("error sending message: " + e.getMessage());
                }
            });
        }
    }

    private void sendGameInfoAysnc(String event, String roomId, String playerId) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        message.put("msg", "restart");
        message.put("playerId", playerId);
        List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        if (event.equals("restartGame")) {
            message.put("participants", gamePlayerEntities);
        }
        String messageJson = objectMapper.writeValueAsString(message);
        List<Future<?>> futures = new ArrayList<>();
        for (WebSocketSession session : socketSessions) {
            Future<?> future = threadPoolExecutor.submit(() -> {
                try {
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("sendGameInfoAysnc error sending message: " + e.getMessage());
                }
            });
            futures.add(future);
        }

        // Wait for all messages to be sent before proceeding
        for (Future<?> future : futures) {
            try {
                future.get();  // Wait for each message sending task to complete
            } catch (InterruptedException | ExecutionException e) {
                log.error("sendGameInfoAysnc error waiting for message send completion: " + e.getMessage());
            }
        }

        if (event.equals("closeGame")) {
            for (WebSocketSession session : socketSessions) {
                removeSession(session);
            }
        }
    }


    // 移除会话并关闭 WebSocket 连接的方法
    private void removeSession(WebSocketSession session) {
        String roomId = session.getAttributes().get("roomId").toString();
        Set<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            // 移除 session
            sessions.remove(session);
            // 如果房间没有其他会话了，移除这个房间的记录
            if (sessions.isEmpty()) {
                roomSessions.remove(roomId);
            }
            // 关闭 WebSocket 连接
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendInfoDataAsync(String event, String roomId, String status) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        message.put("info", status);
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        for (WebSocketSession session : socketSessions) {
            threadPoolExecutor.execute(() -> {
                try {
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("sendInfoDataAsync error sending message: " + e.getMessage());
                }
            });
        }
    }

    private void sendTaskData(String event, String roomId, String playerId, TaskEntity task, boolean isSendToHost) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        RoomEntity roomEntity = roomService.queryRoomById(roomId);
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        for (WebSocketSession session : socketSessions) {
            Map<String, Object> sessionAttributes = session.getAttributes();
            String sessionPlayerId = (String) sessionAttributes.get("playerId");
            Object isHost = sessionAttributes.get("isHost");
            if (isSendToHost) {
                message.put("task", task);
                String messageJson = objectMapper.writeValueAsString(message);
                if (isHost != null) {
                    session.sendMessage(new TextMessage(messageJson));
                }
            }
            if (sessionPlayerId.equals(playerId)) {
                message.put("task", task);
                String messageJson = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(messageJson));
            } else {
                if (isHost == null) {
                    if (roomEntity.getHasHost() == 1) {
                        message.put("task", TaskEntity.builder()
                                .typeInfo(task.getTypeInfo())
                                .build());
                    } else {
                        message.put("task", TaskEntity.builder()
                                .typeInfo(task.getTypeInfo())
                                .subTypeInfo(task.getSubTypeInfo())
                                .build());
                    }
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                }
            }
        }
    }

    private void sendReadyDataAsync(String event, String roomId, String playerId) throws Exception {
        Map<String, Object> message = new HashMap<>();
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        boolean isHostReady = false;
        // 查询最新的房间人员数据
        List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
        for (GamePlayerEntity gamePlayerEntity : gamePlayerEntities) {
            if (gamePlayerEntity.getIsHost() == 1) {
                isHostReady = gamePlayerEntity.getIsReady() == 1 ? true : false;
                break;
            }
        }
        message.put("isHostReady", isHostReady);
        message.put("participants", gamePlayerEntities);
        // 获取 当前房间的人员信息返回给前端
        message.put("playerId", playerId);
        message.put("event", event);
        message.put("roomId", roomId);
        String messageJson = objectMapper.writeValueAsString(message);
        for (WebSocketSession session : socketSessions) {
            threadPoolExecutor.execute(() -> {
                try {
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("sendReadyData error sending message: " + e.getMessage());
                }
            });
        }
    }

    private void sendJoinData(String event, String roomId, String playerId, String info) throws Exception {
        Map<String, Object> message = new HashMap<>();
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        boolean hasHost = false;
        Integer roomNum = 0;
        String hostPlayerId = "";
        String creatorPlayerId = "";
        List<GamePlayerEntity> gamePlayerEntities = new ArrayList<>();
        // 获取 当前房间的人员信息返回给前端
        message.put("playerId", playerId);
        message.put("event", event);
        message.put("roomId", roomId);
        message.put("info", info);
        if ("joinedSuccess".equals(event)) {
            RoomEntity roomEntity = roomService.queryRoomById(roomId);
            hasHost = roomEntity.getHasHost() == 1 ? true : false;
            roomNum = roomEntity.getRoomNum();
            // 查询最新的房间人员数据
            gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
            GamePlayerEntity hostPlayer = gamePlayerEntities.stream()
                    .filter(player -> player.getIsHost() == 1 && player.getIsReady() == 1)
                    .findFirst()
                    .orElse(null);
            if (hasHost) {
                for (GamePlayerEntity gamePlayerEntity : gamePlayerEntities) {
                    if (gamePlayerEntity.getIsHost() == 1) {
                        hostPlayerId = gamePlayerEntity.getPlayerId();
                        break;
                    }
                }
            } else {
                for (GamePlayerEntity gamePlayerEntity : gamePlayerEntities) {
                    if (gamePlayerEntity.getIsCreator() != null && gamePlayerEntity.getIsCreator() == 1) {
                        creatorPlayerId = gamePlayerEntity.getPlayerId();
                        break;
                    }
                }
            }
            message.put("hasHost", hasHost);
            message.put("isHostReady", hostPlayer != null ? true : false);
            message.put("roomNum", roomNum);
            message.put("hostPlayerId", hostPlayerId);
            message.put("creatorPlayerId", creatorPlayerId);
            message.put("participants", gamePlayerEntities);
            String messageJson = objectMapper.writeValueAsString(message);
            for (WebSocketSession session : socketSessions) {
                threadPoolExecutor.execute(() -> {
                    try {
                        session.sendMessage(new TextMessage(messageJson));
                    } catch (Exception e) {
                        log.error("error sending message: " + e.getMessage());
                    }
                });
            }
        } else if ("joinedError".equals(event)) {
            String messageJson = objectMapper.writeValueAsString(message);
            if (socketSessions == null) return;
            for (WebSocketSession session : socketSessions) {
                String sessionPlayerId = session.getAttributes().get("playerId").toString();
                if (playerId.equals(sessionPlayerId)) {
                    session.sendMessage(new TextMessage(messageJson));
                    socketSessions.remove(session);
                    break;
                }
            }
        }
    }

    private void sendLeaveDataAsync(String event, String roomId, String playerId) throws Exception {
        Map<String, Object> message = new HashMap<>();
        Set<WebSocketSession> socketSessions = roomSessions.get(roomId);
        boolean isHostReady = false;
        // 查询最新的房间人员数据
        List<GamePlayerEntity> gamePlayerEntities = gamePlayerService.queryGamePlayers(roomId);
        for (GamePlayerEntity gamePlayerEntity : gamePlayerEntities) {
            if (gamePlayerEntity.getIsHost() == 1) {
                isHostReady = gamePlayerEntity.getIsReady() == 1 ? true : false;
                break;
            }
        }
        message.put("isHostReady", isHostReady);
        message.put("participants", gamePlayerEntities);
        // 获取 当前房间的人员信息返回给前端
        message.put("playerId", playerId);
        message.put("event", event);
        message.put("roomId", roomId);
        String messageJson = objectMapper.writeValueAsString(message);
        for (WebSocketSession session : socketSessions) {
            threadPoolExecutor.execute(() -> {
                try {
                    session.sendMessage(new TextMessage(messageJson));
                } catch (Exception e) {
                    log.error("sendLeaveDataAsync error sending message: " + e.getMessage());
                }
            });
        }
        // session 移除
        WebSocketSession currentSession = null;
        Set<WebSocketSession> webSocketSessions = roomSessions.get(roomId);
        for (WebSocketSession webSocketSession : webSocketSessions) {
            String sessionPlayerId = webSocketSession.getAttributes().get("playerId").toString();
            if (sessionPlayerId.equals(playerId)) {
                currentSession = webSocketSession;
                break;
            }
        }
        removeSession(currentSession);
    }

    private void sendRoleData(String event, List<GamePlayerEntity> entities) throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("event", event);
        Set<WebSocketSession> webSocketSessions = roomSessions.get(entities.get(0).getRoomId());
        if ("playerRole".equals(event)) {
            for (WebSocketSession session : webSocketSessions) {
                Map<String, Object> sessionAttributes = session.getAttributes();
                if (sessionAttributes.get("isHost") == null) {
                    String playerId = sessionAttributes.get("playerId").toString();
                    // 执行深拷贝
                    List<GamePlayerEntity> copiedPlayers  = getPlayerByplayerId(entities,playerId);
                    // 发送给玩家个人的角色信息
                    message.put("roleInfo", copiedPlayers);
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                }
            }
        } else {
            for (WebSocketSession session : webSocketSessions) {
                Map<String, Object> sessionAttributes = session.getAttributes();
                Object isHost = sessionAttributes.get("isHost");
                if (isHost != null) {
                    message.put("roleInfo", entities);
                    String messageJson = objectMapper.writeValueAsString(message);
                    session.sendMessage(new TextMessage(messageJson));
                }
            }
        }
    }

    private List<GamePlayerEntity> getPlayerByplayerId(List<GamePlayerEntity> entities,String playerId){
        // 执行深拷贝
        List<GamePlayerEntity> copiedPlayers = DeepCopyUtil.deepCopy(entities);
        // 隐藏其他玩家的角色信息
        for (GamePlayerEntity gamePlayer : copiedPlayers) {
            if (!playerId.equals(gamePlayer.getPlayerId())) {
                gamePlayer.setRoleName("");  // 或 gamePlayer.setRoleName("");
            }
        }
        return copiedPlayers;
    }

    private Map<String, Object> joinRoom(GamePlayerEntity entity) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            String roomId = entity.getRoomId();
            String playerId = entity.getPlayerId();
            log.info("playerId :{} 加入房间 , roomId:{} ", playerId, roomId);
            // 查询该玩家是否已经加入
            GamePlayerEntity gamePlayerEntity = gamePlayerService.queryGamePlayer(roomId, playerId);
            if (gamePlayerEntity != null) {
                log.info("房间 roomId:{}  玩家已加入", roomId);
                returnMap.put("code", "error");
                returnMap.put("info", "已经在房间了鱿~");
                return returnMap;
            }
            String roomStatus = tokenService.useToken(TokenVO.builder()
                    .token(roomId)
                    .build());
            if ("YES".equals(roomStatus)) {
                // 1. 加入比赛玩家表
                gamePlayerService.savePlayer(GamePlayerEntity.builder()
                        .isReady(0)
                        .isAlive(1)
                        .isCreator(0)
                        .playerId(playerId)
                        .isHost(0)
                        .isCreator(0)
                        .roomId(roomId)
                        .build());
                returnMap.put("code", "success");
                returnMap.put("info", "加入成功");
            } else {
                log.info("房间 roomId:{} - {}", roomId, roomStatus);
                returnMap.put("code", "error");
                returnMap.put("info", roomStatus);
            }
        } catch (Exception e) {
            log.error("加入房间失败 token:{}", entity.getRoomId(), e);
            log.info("房间 roomId:{}  房间不存在", entity.getRoomId());
            returnMap.put("code", "error");
            returnMap.put("info", "房间不存在");
        }
        return returnMap;
    }

    private void updatePlayer(GamePlayerEntity entity) {
        gamePlayerService.updatePlayer(entity);
    }

    private void removePlayer(GamePlayerEntity entity) {
        tokenService.releaseToken(TokenVO.builder()
                .token(entity.getRoomId())
                .build());
        gamePlayerService.deletePlayer(entity.getRoomId(), entity.getPlayerId());
    }
}
