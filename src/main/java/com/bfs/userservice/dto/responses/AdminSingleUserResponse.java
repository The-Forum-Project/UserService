package com.bfs.userservice.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdminSingleUserResponse {
    Long userId;
    String firstName;
    String lastName;
    String email;
    LocalDateTime registrationDate;
    Boolean active;
    Integer type;

}
