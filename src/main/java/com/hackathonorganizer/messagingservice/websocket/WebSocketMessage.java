package com.hackathonorganizer.messagingservice.websocket;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class WebSocketMessage {

    private String from;

    private String type;

    private String data;

    private Object candidate;

    private Object sdp;
}
