package com.bfs.hibernateprojectdemo.controller;

import com.bfs.hibernateprojectdemo.domain.User;
import com.bfs.hibernateprojectdemo.dto.responses.IdUserResponse;
import com.bfs.hibernateprojectdemo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<IdUserResponse> getUserById(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        User user = userService.getUserById(id);

        if (authorities.stream().noneMatch(authority -> authority.getAuthority().equals("admin"))) {
            if (!user.getUserId().equals(authentication.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        IdUserResponse.builder()
                                .message("You are not authorized to access this content")
                                .build()
                );
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(
                        IdUserResponse.builder()
                                .message("Successfully retrieved user info")
                                .firstName(user.getFirstName())
                                .lastName(user.getLastName())
                                .registrationDate(user.getDateJoined())
                                .build()
                );
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    IdUserResponse.builder()
                            .message("Successfully retrieved user info")
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .registrationDate(user.getDateJoined())
                            .build()
            );
        }
    }

}
