package com.bfs.hibernateprojectdemo.dto;

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
    @NotBlank(message = "email cannot be blank")
    String email;
    @NotBlank(message = "code cannot be blank")
    String code;
}
