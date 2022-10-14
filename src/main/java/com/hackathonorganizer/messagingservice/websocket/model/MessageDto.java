package com.hackathonorganizer.messagingservice.websocket.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class MessageDto {

    MessageType messageType;
    Object data;

    @Override
    public String toString() {
        return "BaseMessage{" +
                "messageType=" + messageType +
                ", data=" + data.toString() +
                '}';
    }
}
