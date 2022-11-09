package com.hackathonorganizer.messagingservice.websocket.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hackathonorganizer.messagingservice.exception.SignalServerException;
import com.hackathonorganizer.messagingservice.websocket.model.dto.ChatEntryDto;
import com.hackathonorganizer.messagingservice.websocket.model.Message;
import com.hackathonorganizer.messagingservice.websocket.model.*;
import com.hackathonorganizer.messagingservice.websocket.model.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;


import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignalHandler extends TextWebSocketHandler  {
    
    private final Map<Long, List<UserSession>> teamRooms = new HashMap<>();
    private final MessageService messageService;
    private Map<String, String> queryParams;
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule()).build();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {

        Long chatId = getChatIdFromQueryParams();

        MessageDto messageDto = objectMapper.readValue(message.getPayload(), MessageDto.class);

        switch (messageDto.messageType()) {

            case OFFER, ANSWER, ICE_CANDIDATE -> sendSignalToChatParticipants(chatId, session, messageDto);
            case MESSAGE -> sendMessageToChatParticipants(messageDto);
            default -> throw new UnsupportedOperationException("Unsupported message type");
        }
    }

    private void sendSignalToChatParticipants(Long chatId,
            WebSocketSession session, MessageDto messageDto) throws JsonProcessingException {

        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(messageDto));

        this.teamRooms.get(chatId).forEach(s -> {
            try {
                if (s.getSession().isOpen() && !session.getId().equals(s.getSession().getId())) {
                    s.getSession().sendMessage(textMessage);
                }
            } catch (IOException ex) {

                log.warn("Sending signal to chat participants failed: \n {}", ex.getMessage());

                throw new SignalServerException(
                        String.format("Sending signal to chat participants failed: \n %s", ex.getMessage())
                );
            }
        });
    }

    private void sendMessageToChatParticipants(MessageDto messageDto) throws JsonProcessingException {

        Message message = objectMapper.convertValue(messageDto.data(), Message.class);

        ChatEntryDto chatEntryDto = messageService.saveChatMessage(message);

        MessageDto messageResponse = new MessageDto(MessageType.MESSAGE, chatEntryDto);

        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(messageResponse));

        this.teamRooms.get(message.getChatId()).forEach(s -> {
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

    private void storeUserSession(Long chatId, String username, WebSocketSession session) {

        UserSession userSession = new UserSession(username, session);

        if (teamRooms.get(chatId) != null) {
            teamRooms.get(chatId).add(userSession);
        } else {
            List<UserSession> sessionsList = new ArrayList<>();
            sessionsList.add(userSession);
            teamRooms.put(chatId, sessionsList);
        }
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
