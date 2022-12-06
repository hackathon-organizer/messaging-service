package com.hackathonorganizer.messagingservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties
public class VaultProperties {

    @Value("${openvidu.host}")
    private String openViduHost;

    @Value("${openvidu.secret}")
    private String openViduSecret;
}
