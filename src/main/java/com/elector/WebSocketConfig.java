package com.elector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.elector.Utils.Definitions.KEEP_ALIVE_MESSAGE;
import static com.elector.Utils.Definitions.MINUTE;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfigurer.class);
    private static int totalSessions;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(new MessagesHandler(), "/stream");
    }

    @Scheduled (fixedRate = 5 * MINUTE)
    public void logActiveSessions () {
        LOGGER.info("current total active sessions: {}", totalSessions);
    }


    class MessagesHandler extends TextWebSocketHandler {

        private List<WebSocketSession> sessionList = new CopyOnWriteArrayList<>();


        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            super.afterConnectionEstablished(session);
            sessionList.add(session);
            totalSessions = sessionList.size();
            LOGGER.info("session opened");
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            super.handleTextMessage(session, message);
            if (!message.getPayload().equals(KEEP_ALIVE_MESSAGE)) {
                for (WebSocketSession socketSession : sessionList) {
                    socketSession.sendMessage(message);
                }
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            super.afterConnectionClosed(session, status);
            sessionList.remove(session);
            totalSessions = sessionList.size();
            LOGGER.info("session closed");

        }
    }

}
