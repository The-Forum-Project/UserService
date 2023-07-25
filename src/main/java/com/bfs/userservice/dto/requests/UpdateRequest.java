package com.bfs.userservice.dto.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateRequest {

    @Size(max = 255, message = "firstName must have fewer then 255 characters")
    private String firstName;

    @Size(max = 255, message = "lastName must have fewer then 255 characters")
    private String lastName;

    @Size(max = 255, message = "email must have fewer then 255 characters")
    @Email(message = "email should be valid")
    private String email;

    @Size(max = 255, message = "password must have fewer then 255 characters")
    private String password;

    @Size(max = 255, message = "profileImageURL must have fewer then 255 characters")
    private String profileImageURL;

}
