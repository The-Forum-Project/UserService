package com.bfs.userservice.controller;

import com.bfs.userservice.domain.User;
import com.bfs.userservice.dto.requests.UpdateRequest;
import com.bfs.userservice.dto.CodeRequest;
import com.bfs.userservice.dto.EmailRequest;
import com.bfs.userservice.dto.SimpleMessage;
import com.bfs.userservice.dto.responses.IdUserResponse;
import com.bfs.userservice.dto.responses.MessageResponse;
import com.bfs.userservice.service.RedUserService;
import com.bfs.userservice.util.SerializeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final RedUserService redUserService;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public UserController(RedUserService redUserService, RabbitTemplate rabbitTemplate) {
        this.redUserService = redUserService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<IdUserResponse> getUserById(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();


        if (authorities.stream().noneMatch(authority -> authority.getAuthority().equals("admin"))) {
            if (!id.equals(authentication.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        IdUserResponse.builder()
                                .message("You are not authorized to access this content")
                                .build()
                );
            }
        }

        User user = redUserService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    IdUserResponse.builder()
                            .message("User does not exist!")
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

    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateRequest request, BindingResult result) {

        if (result.hasErrors()) {
            String message = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            MessageResponse messageResponse = MessageResponse.builder()
                    .message(message)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageResponse);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();

        if (authorities.stream().noneMatch(authority -> authority.getAuthority().equals("admin"))) {
            if (!id.equals(authentication.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        MessageResponse.builder()
                                .message("You are not authorized to update this content")
                                .build()
                );
            }
        }

        User updatedUser = User.builder()
                .userId(id)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .profileImageURL(request.getProfileImageURL())
                .build();

        Boolean addSuccess = redUserService.updateUser(updatedUser);
        String message;

        MessageResponse messageResponse;
        if (!addSuccess) {
            message = "User does not exist!";
            messageResponse = MessageResponse.builder()
                    .message(message)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);

        } else {
            message = "Success! The User was updated.";
            messageResponse = MessageResponse.builder()
                    .message(message)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
        }

    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();


        if (authorities.stream().noneMatch(authority -> authority.getAuthority().equals("admin"))) {
            if (!id.equals(authentication.getPrincipal())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        MessageResponse.builder()
                                .message("You are not authorized to delete this content")
                                .build()
                );
            }
        }

        User user = redUserService.getUserById(id);
        Boolean deleteSuccess = redUserService.deleteUser(user);

        String message;
        MessageResponse messageResponse;
        if (!deleteSuccess) {
            message = "User does not exist!";
            messageResponse = MessageResponse.builder()
                    .message(message)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse);

        } else {
            message = "Success! The User was deleted.";
            messageResponse = MessageResponse.builder()
                    .message(message)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
        }

    }

    @PatchMapping("verify/email")
    public ResponseEntity<String> produceDirect(@Valid @RequestBody EmailRequest request,
                                                @RequestParam String routingKey){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        SimpleMessage newMessage = SimpleMessage.builder()
                .email(request.getEmail())
                .code(redUserService.updateCode(userId))
                .build();

        String jsonMessage = SerializeUtil.serialize(newMessage);

        rabbitTemplate.convertAndSend("demo.direct", routingKey, jsonMessage);

        return ResponseEntity.ok("Email Sent");
    }

    @PostMapping("verify/code")
    public ResponseEntity<String> verifyCode(@Valid @RequestBody CodeRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        String email = request.getEmail();
        String code = request.getCode();
        Boolean success = redUserService.updateEmail(userId, email, code);
        if (!success) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("The code is incorrect");
        }
        return ResponseEntity.ok("Verified Successfully");
    }

}