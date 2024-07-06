package ink.squidkill.trigger.http;

import ink.squidkill.domain.task.model.entity.TaskEntity;
import ink.squidkill.domain.task.model.valobj.TaskVO;
import ink.squidkill.domain.task.service.ITaskService;
import ink.squidkill.trigger.api.ITaskDTOService;
import ink.squidkill.trigger.api.dto.RodomTaskResponseDTO;
import ink.squidkill.trigger.api.dto.TaskRequestDTO;
import ink.squidkill.trigger.api.dto.TaskResponseDTO;
import ink.squidkill.types.common.Constants;
import ink.squidkill.types.enums.ResponseCode;
import ink.squidkill.types.model.Response;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Description: 任务列表请求
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 14:36
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/task/")
public class TaskController implements ITaskDTOService {

    @Resource
    private ITaskService taskService;


    @RequestMapping(value = "getTasks", method = RequestMethod.POST)
    @Override
    public Response<TaskResponseDTO> getTaskList(@RequestBody TaskRequestDTO request) {
        try{
            String types = request.getTypes();
            String[] typeList = types.split(Constants.SPLIT);
            List<TaskEntity> taskEntities = taskService.queryTasksByType(Arrays.asList(typeList));

            Response<TaskResponseDTO> response = Response.<TaskResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .result(TaskResponseDTO.builder()
                            .tasks(taskEntities)
                            .build())
                    .build();

            return response;

        }
        catch (Exception e) {
            return Response.<TaskResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @Override
    public Response<RodomTaskResponseDTO> lotteryTask(TaskRequestDTO request) {
        try{
            TaskEntity taskEntity = taskService.lotteryTask(TaskVO.builder()
                    .models(request.getModels())
                    .gameTypes(request.getGameTypes())
                    .levels(request.getLevels()).build());


            Response<RodomTaskResponseDTO> response = Response.<RodomTaskResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .result(RodomTaskResponseDTO.builder()
                            .level(taskEntity.getLevel())
                            .type(taskEntity.getType())
                            .subType(taskEntity.getSubType())
                            .title(taskEntity.getTitle())
                            .subTitle(taskEntity.getSubTitle())
                            .typeInfo(taskEntity.getTypeInfo())
                            .subTypeInfo(taskEntity.getSubTypeInfo())
                            .build())
                    .build();

            return response;
        }
        catch (Exception e) {
            return Response.<RodomTaskResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
