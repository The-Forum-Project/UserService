package com.bfs.userservice.dto;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
public class UserNullable {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String profileImageURL;
}
