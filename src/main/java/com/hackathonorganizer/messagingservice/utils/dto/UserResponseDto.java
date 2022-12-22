package com.hackathonorganizer.messagingservice.utils.dto;

public record UserResponseDto(
        Long id,

        String username,

        Long currentHackathonId,

        Long currentTeamId
) {
}
