package com.bfs.userservice.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    @NotBlank(message = "email cannot be blank")
    String email;
}
