package com.bfs.hibernateprojectdemo.dto.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdateRequest {

    @NotBlank(message = "firstName cannot be blank")
    @Size(max = 255, message = "firstName must have fewer then 255 characters")
    private String firstName;

    @NotBlank(message = "lastName cannot be blank")
    @Size(max = 255, message = "lastName must have fewer then 255 characters")
    private String lastName;

    @NotBlank(message = "email cannot be blank")
    @Size(max = 255, message = "email must have fewer then 255 characters")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(max = 255, message = "password must have fewer then 255 characters")
    private String password;

    @NotBlank(message = "profileImageURL cannot be blank")
    @Size(max = 255, message = "profileImageURL must have fewer then 255 characters")
    private String profileImageURL;

}
