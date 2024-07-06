package ink.squidkill.config;

import ink.squidkill.trigger.listener.GameHandshakeInterceptor;
import ink.squidkill.trigger.ws.handle.GameWebSocketHandler;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Description: WebSocket配置
 *
 * @author qqilin1213
 * @version 1.0
 * @date 2024/6/18 13:59
 */

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final GameWebSocketHandler gameWebSocketHandler;

    public WebSocketConfig(GameWebSocketHandler gameWebSocketHandler) {
        this.gameWebSocketHandler = gameWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameWebSocketHandler, "/websocket")
                .addInterceptors(new GameHandshakeInterceptor())
                .setAllowedOriginPatterns("*");
    }
}




