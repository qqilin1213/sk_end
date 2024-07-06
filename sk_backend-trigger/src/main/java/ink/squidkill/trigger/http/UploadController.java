package ink.squidkill.trigger.http;

import ink.squidkill.domain.room.model.entity.RoomEntity;
import ink.squidkill.domain.room.service.IRoomService;
import ink.squidkill.trigger.ws.handle.GameWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.Base64Utils;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/27 14:05
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/upload/")
public class UploadController {
    @Resource
    GameWebSocketHandler gameWebSocketHandler;

    @Resource
    IRoomService roomService;

    @RequestMapping(value = "file", method = RequestMethod.POST)
    public void upload(@RequestParam("file") MultipartFile file,
                                           @RequestParam("roomId") String roomId){
        try{
            String base64EncodedImage = generateBase64(file);
            roomService.updateRoom(RoomEntity.builder()
                    .roomId(roomId)
                    .resultImg(base64EncodedImage)
                    .build());
            gameWebSocketHandler.sendImage(roomId,base64EncodedImage);
        }catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    /**
     * 将MultipartFile 图片文件编码为base64
     * @param file
     * @return
     * @throws Exception
     */
    public String generateBase64(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        String contentType = file.getContentType();
        byte[] imageBytes = null;
        String base64EncoderImg="";
        try {
            imageBytes = file.getBytes();
            BASE64Encoder base64Encoder =new BASE64Encoder();
            /**
             * 1.Java使用BASE64Encoder 需要添加图片头（"data:" + contentType + ";base64,"），
             *   其中contentType是文件的内容格式。
             * 2.Java中在使用BASE64Enconder().encode()会出现字符串换行问题，这是因为RFC 822中规定，
             *   每72个字符中加一个换行符号，这样会造成在使用base64字符串时出现问题，
             *   所以我们在使用时要先用replaceAll("[\\s*\t\n\r]", "")解决换行的问题。
             */
            base64EncoderImg = "data:" + contentType + ";base64," + base64Encoder.encode(imageBytes);
            base64EncoderImg = base64EncoderImg.replaceAll("[\\s*\t\n\r]", "");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return base64EncoderImg;
    }

}
