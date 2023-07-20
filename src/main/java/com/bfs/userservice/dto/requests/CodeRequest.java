package com.bfs.userservice.dto.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeRequest {
    @NotBlank(message = "code cannot be blank")
    String code;
}
