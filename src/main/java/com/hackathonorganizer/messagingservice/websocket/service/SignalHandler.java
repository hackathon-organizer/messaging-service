package com.hackathonorganizer.messagingservice.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hackathonorganizer.messagingservice.configuration.VaultProperties;
import com.hackathonorganizer.messagingservice.exception.SignalServerException;
import com.hackathonorganizer.messagingservice.websocket.model.dto.ChatEntryDto;
import com.hackathonorganizer.messagingservice.websocket.model.Message;
import com.hackathonorganizer.messagingservice.websocket.model.*;
import com.hackathonorganizer.messagingservice.websocket.model.dto.MessageDto;
import com.hackathonorganizer.messagingservice.websocket.model.dto.UserSessionDto;
import io.openvidu.java.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;


import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Slf4j
@Service
@EnableConfigurationProperties(VaultProperties.class)
public class SignalHandler extends TextWebSocketHandler  {
    
    private final Map<Long, List<UserSession>> teamRooms = new HashMap<>();

    private MessageService messageService;
    private Map<String, String> queryParams;
    private ObjectMapper objectMapper;
    private OpenVidu openvidu;

    public SignalHandler() {
    }

    public SignalHandler(VaultProperties vaultProperties, MessageService messageService) {
        this.messageService = messageService;

        this.objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

        this.openvidu = new OpenVidu(vaultProperties.getOpenViduHost(), vaultProperties.getOpenViduSecret());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        MessageDto messageDto = objectMapper.readValue(message.getPayload(), MessageDto.class);

        if (messageDto.messageType() == MessageType.MESSAGE || messageDto.messageType() == MessageType.VIDEO_IN_PROGRESS) {
            sendMessageToChatParticipants(messageDto);
        }
    }

    private void sendMessageToChatParticipants(MessageDto messageDto) throws JsonProcessingException {

        TextMessage textMessage;
        Message message;

        Long chatId = getChatIdFromQueryParams();

        if (messageDto.messageType().equals(MessageType.VIDEO_IN_PROGRESS)) {
            textMessage = new TextMessage(objectMapper.writeValueAsString(messageDto));
        } else {

        message = objectMapper.convertValue(messageDto.data(), Message.class);

        ChatEntryDto chatEntryDto = messageService.saveChatMessage(message);

        MessageDto messageResponse = new MessageDto(MessageType.MESSAGE, chatEntryDto);

        textMessage = new TextMessage(objectMapper.writeValueAsString(messageResponse));
        }

        this.teamRooms.get(chatId).forEach(s -> {
            try {
                s.getSession().sendMessage(textMessage);
            } catch (IOException ex) {

                log.warn("Sending signal to chat participants failed: \n {}", ex.getMessage());

                throw new SignalServerException(
                        String.format("Sending signal to chat participants failed: \n %s", ex.getMessage())
                );
            }
        });
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.info("Session {} connected", session.getId());

        if (session.getUri() == null) {

            log.warn("Session: {} uri is null", session.getUri());

            throw new SignalServerException(
                    String.format("Session: %s uri is null", session.getUri())
            );
        }

        String sessionUri = session.getUri().toString();

        queryParams = UriComponentsBuilder.fromUriString(sessionUri)
                        .build().getQueryParams().toSingleValueMap();

        String username = getUsernameFromQueryParams();
        Long chatId = getChatIdFromQueryParams();

        storeUserSession(chatId, username, session);

        sendUserJoinNotification(chatId);
    }

    private void storeUserSession(Long chatId, String username, WebSocketSession session) throws OpenViduJavaClientException, OpenViduHttpException, JsonProcessingException {

        UserSession userSession = new UserSession(username, session);

        if (teamRooms.get(chatId) != null && teamRooms.get(chatId).size() > 0) {

            UserSession masterSession = teamRooms.get(chatId).get(0);

            String sessionId = masterSession.getVideoSessionId();

            userSession.setVideoSessionId(sessionId);
            userSession.setVideoSessionToken(getUserVideoSessionToken(sessionId));

            teamRooms.get(chatId).add(userSession);
            sendVideoSessionDetails(userSession);
        } else {
            List<UserSession> sessionsList = new ArrayList<>();
            sessionsList.add(userSession);

            String videoSessionId = createNewVideoSession();

            userSession.setVideoSessionId(videoSessionId);
            userSession.setVideoSessionToken(getUserVideoSessionToken(videoSessionId));

            teamRooms.put(chatId, sessionsList);
            sendVideoSessionDetails(userSession);
        }
    }

    private void sendVideoSessionDetails(UserSession userSession) throws JsonProcessingException {

        log.error(userSession.toString());

        UserSessionDto userSessionDto = new UserSessionDto(
                userSession.getVideoSessionId(),
                userSession.getVideoSessionToken()
        );

        log.error(userSessionDto.toString());

        MessageDto messageDto = new MessageDto(MessageType.SESSION, userSessionDto);

        TextMessage chatSessions = new TextMessage(objectMapper.writeValueAsString(messageDto));

            try {
                userSession.getSession().sendMessage(chatSessions);
            } catch (IOException ex) {

                log.warn("Sending session data to user failed: {}", ex.getMessage());

                throw new SignalServerException(String.format("Sending session data to user failed: %s", ex.getMessage()));
            }
    }

    private String createNewVideoSession() throws OpenViduJavaClientException, OpenViduHttpException {
        UUID uuid = UUID.randomUUID();
        SessionProperties sessionProperties = new SessionProperties.Builder().customSessionId(uuid.toString()).build();

        return openvidu.createSession(sessionProperties).getSessionId();
    }

    private String getUserVideoSessionToken(String sessionId) throws OpenViduJavaClientException, OpenViduHttpException {

        Session session = openvidu.getActiveSession(sessionId);
        if (session == null) {
            throw new SignalServerException("Session with id " + sessionId + " not found");
        }
        Connection connection = session.createConnection();
        return connection.getToken();
    }

    private void sendUserJoinNotification(Long chatId) throws IOException {

        List<String> sessionUsernamesList =
                this.teamRooms.get(chatId).stream().map(UserSession::getUsername).toList();

        MessageDto messageDto = new MessageDto(MessageType.JOIN, sessionUsernamesList);

        TextMessage chatSessions = new TextMessage(objectMapper.writeValueAsString(messageDto));

        this.teamRooms.get(chatId).forEach(s -> {
            try {
                s.getSession().sendMessage(chatSessions);
            } catch (IOException ex) {

                log.warn("Sending signal to chat participants failed: \n {}", ex.getMessage());

                throw new SignalServerException(
                        String.format("Sending signal to chat participants failed: \n %s", ex.getMessage())
                );
            }
        });
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.warn(status.toString());

        Long chatId = Long.parseLong(queryParams.get("chatId"));

        this.teamRooms.get(chatId).removeIf(userSession ->
                userSession.getSession().getId().equals(session.getId()));

        log.info("Closed session {}", session.getId());
    }

    private Long getChatIdFromQueryParams() {
        return Long.parseLong(queryParams.get("chatId"));
    }

    private String getUsernameFromQueryParams() {
        return queryParams.get("username");
    }
}
