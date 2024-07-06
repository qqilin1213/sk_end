package ink.squidkill.trigger.listener;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Description: new java files header..
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/22 23:03
 */
public class GameHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String playerId = servletRequest.getParameter("playerId");
        String roomId = servletRequest.getParameter("roomId");
        String isHost = servletRequest.getParameter("isHost");
        String reconnect = servletRequest.getParameter("reconnect");
        if (playerId != null ) {
            attributes.put("playerId", playerId);
        }
        if (roomId != null ) {
            attributes.put("roomId", roomId);
        }
        if(isHost != null){
            attributes.put("isHost", true);
        }
        if(reconnect != null){
            attributes.put("reconnect", true);
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}