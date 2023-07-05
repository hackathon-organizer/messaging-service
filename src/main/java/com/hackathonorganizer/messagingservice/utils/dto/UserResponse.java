package com.hackathonorganizer.messagingservice.utils.dto;

public record UserResponse(

        Long id,
        String username,
        Long currentHackathonId,
        Long currentTeamId
) {
}
