package com.bfs.userservice.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class IdUserResponse {
    String message;
    String firstName;
    String lastName;
    LocalDateTime registrationDate;
    String imageURL;
    Long userId;
}
