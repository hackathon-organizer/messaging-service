package com.hackathonorganizer.messagingservice.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathonorganizer.messagingservice.chat.model.Message;
import com.hackathonorganizer.messagingservice.websocket.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;


import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class SignalHandler extends TextWebSocketHandler  {



//    List<WebSocketSession>sessions = new LinkedList<WebSocketSession>();
//    ConcurrentHashMap<String,WebSocketSession> sessionMap = new ConcurrentHashMap<String,WebSocketSession>();
//    final ObjectMapper map1=new ObjectMapper();

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private Map<Long, List<WebSocketSession>> teamRooms = new HashMap<>();
    private final MessageService messageService;

    @Autowired
    public SignalHandler(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {

        log.info(session.toString());

        Message myObject = new ObjectMapper()
                .readValue(message.getPayload(), Message.class);
        myObject.setCreatedAt(LocalDateTime.now());

        messageService.saveChatMessage(myObject);

        List<WebSocketSession> x =
                teamRooms.get(myObject.getChatId()) == null ?
                        new ArrayList<>() : teamRooms.get(myObject.getChatId());
        x.add(session);

        this.teamRooms.put(myObject.getChatId(), x);

        System.out.println(session.getAttributes().toString());

//        for (WebSocketSession webSocketSession : sessions) {
//            //if (webSocketSession.isOpen() && !session.getId().equals
//            // (webSocketSession.getId())) {
//
//                webSocketSession.sendMessage(message);
//            //}
//        }

        this.teamRooms.get(myObject.getChatId()).forEach(s -> {
            try {
                s.sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Add session {}", session.getId());

        sessions.add(session);

                for (WebSocketSession webSocketSession : sessions) {
            //if (webSocketSession.isOpen() && !session.getId().equals
            // (webSocketSession.getId())) {

                    TextMessage textMessage = new TextMessage("User join chat");
                    System.out.println('x');

                // webSocketSession.sendMessage(textMessage);
            //}
        }
    }

    @AllArgsConstructor
    class TeamRoom {
        WebSocketSession userSession;
        Long roomId;
    }



//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//
//        System.out.println(session.getId());
//
//        final String  msg1=message.getPayload();
//        SignalData sigData=map1.readValue(msg1, SignalData.class);
//        log.debug("Receive message from client:",msg1);
//
//        SignalData sigResp=new SignalData();
//
//        if(sigData.getType().equalsIgnoreCase(SignalType.Login.toString()))	{
//            SignalData sigResp2=new SignalData();
//            String userId=UUID.randomUUID().toString();
//            sigResp2.setUserId("signaling");
//            sigResp2.setType(SignalType.UserId.toString());
//            sigResp2.setData(userId);
//            sessionMap.put(userId, session);
//            session.sendMessage(new TextMessage(map1.writeValueAsString(sigResp2)));
//
//            return ;
//        }
//        else if(sigData.getType().equalsIgnoreCase(SignalType.NewMember.toString()))	{
//
//            sessionMap.values().forEach(a->{
//
//                SignalData sigResp2=new SignalData();
//                sigResp2.setUserId(sigData.getUserId());
//                sigResp2.setType(SignalType.NewMember.toString());
//                try	{
//                    //Check if websocket is open
//                    if(a.isOpen())	{
//                        log.debug("Sending New Member from",sigData.getUserId());
//                        a.sendMessage(new TextMessage(map1.writeValueAsString(sigResp2)));
//                    }
//                }
//                catch(Exception e)	{
//                    log.error("Error Sending message:",e);
//                }
//            });
//
//
//            return ;
//        }
//        else if(sigData.getType().equalsIgnoreCase(SignalType.Offer.toString()))	{
//            sigResp=new SignalData();
//            sigResp.setUserId(sigData.getUserId());
//            sigResp.setType(SignalType.Offer.toString());
//            sigResp.setData(sigData.getData());
//            sigResp.setToUid(sigData.getToUid());
//            sessionMap.get(sigData.getToUid()).sendMessage(new TextMessage(map1.writeValueAsString(sigResp)));
//
//
//        }
//        else if(sigData.getType().equalsIgnoreCase(SignalType.Answer.toString()))	{
//            sigResp=new SignalData();
//            sigResp.setUserId(sigData.getUserId());
//            sigResp.setType(SignalType.Answer.toString());
//            sigResp.setData(sigData.getData());
//            sigResp.setToUid(sigData.getToUid());
//            sessionMap.get(sigData.getToUid()).sendMessage(new TextMessage(map1.writeValueAsString(sigResp)));
//
//
//        }
//        else if(sigData.getType().equalsIgnoreCase(SignalType.Ice.toString()))	{
//            sigResp=new SignalData();
//            sigResp.setUserId(sigData.getUserId());
//            sigResp.setType(SignalType.Ice.toString());
//            sigResp.setData(sigData.getData());
//            sigResp.setToUid(sigData.getToUid());
//            sessionMap.get(sigData.getToUid()).sendMessage(new TextMessage(map1.writeValueAsString(sigResp)));
//
//
//        }
//
//
//    }
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//
//        sessions.add(session);
//        super.afterConnectionEstablished(session);
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//
//        sessions.remove(session);
//        super.afterConnectionClosed(session, status);
//    }

}
