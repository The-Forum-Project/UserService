package com.bfs.userservice.dto.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AdminAllUserResponse {
    String message;
    List<AdminSingleUserResponse> users;
}
