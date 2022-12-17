package com.hackathonorganizer.messagingservice.utils.dto;

import java.util.Set;

public record UserResponseDto(
        Long id,

        String username,

        Long currentHackathonId,

        Long currentTeamId
) {
}
