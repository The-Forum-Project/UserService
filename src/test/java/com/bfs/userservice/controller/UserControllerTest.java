package com.bfs.userservice.controller;


import com.bfs.userservice.config.SecurityConfig;
import com.bfs.userservice.dao.UserDao;
import com.bfs.userservice.domain.User;
import com.bfs.userservice.dto.UserNullable;
import com.bfs.userservice.dto.requests.CodeRequest;
import com.bfs.userservice.dto.requests.UpdateActiveRequest;
import com.bfs.userservice.dto.requests.UpdateRequest;
import com.bfs.userservice.dto.responses.AdminSingleUserResponse;
import com.bfs.userservice.dto.responses.MessageResponse;
import com.bfs.userservice.security.JwtFilter;
import com.bfs.userservice.security.JwtProvider;
import com.bfs.userservice.service.RedUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = UserController.class)
@WebMvcTest(value = UserController.class, excludeAutoConfiguration = SecurityConfig.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
//@SpringBootTest
//@AutoConfigureMockMvc
//@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RedUserService redUserService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private UserDao userDao;

//    @MockBean
//    private SecurityConfig securityConfig;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private JwtFilter jwtFilter;

    //@WithMockUser(roles = "normal")
    @Test
    public void getAllUsersTest() throws Exception {
        // Given
        User user = new User(); // Set your user properties accordingly
        user.setFirstName("Super");
        user.setLastName("Admin");
        user.setEmail("super@email.com");
        String dateTimeStr = "2020-01-01 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        user.setDateJoined(dateTime);
        user.setActive(true);
        user.setUserId(1l);
        user.setType(0);
        Mockito.when(redUserService.getAllUsers()).thenReturn(Arrays.asList(user));
        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/users")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlckBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoic3VwZXIifSx7ImF1dGhvcml0eSI6ImFkbWluIn0seyJhdXRob3JpdHkiOiJub3JtYWwifSx7ImF1dGhvcml0eSI6InVudmVyaWZpZWQifV0sImlkIjoxfQ.C8JuQwGBBN9FiJfI-Xan5BB1B3OBL5G1QEAM0RGPv_k")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"Successfully retrieved all users\",\n" +
                        "    \"users\": [\n" +
                        "        {\n" +
                        "            \"userId\": 1,\n" +
                        "            \"firstName\": \"Super\",\n" +
                        "            \"lastName\": \"Admin\",\n" +
                        "            \"email\": \"super@email.com\",\n" +
                        "            \"registrationDate\": \"2020-01-01T00:00:00\",\n" +
                        "            \"active\": true,\n" +
                        "            \"type\": 0\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}"));
    }
    @Test
    public void getAllUsersTestWithError() throws Exception {
        // Given
        User user = new User(); // Set your user properties accordingly
        user.setFirstName("Super");
        user.setLastName("Admin");
        user.setEmail("super@email.com");
        String dateTimeStr = "2020-01-01 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        user.setDateJoined(dateTime);
        user.setActive(true);
        user.setUserId(1l);
        user.setType(0);
        Mockito.when(redUserService.getAllUsers()).thenReturn(Arrays.asList(user));
        Authentication authentication = new TestingAuthenticationToken("user", "password", "normal");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // When & Then
        mvc.perform(MockMvcRequestBuilders.get("/users")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlckBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoic3VwZXIifSx7ImF1dGhvcml0eSI6ImFkbWluIn0seyJhdXRob3JpdHkiOiJub3JtYWwifSx7ImF1dGhvcml0eSI6InVudmVyaWZpZWQifV0sImlkIjoxfQ.C8JuQwGBBN9FiJfI-Xan5BB1B3OBL5G1QEAM0RGPv_k")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"You are not authorized to access this content\"\n" +
                        "}"));
    }
    @Test
    public void testGetUserById() throws Exception {
        // given
        Long id = 1L;
        User user = new User();
        user.setUserId(id);
        user.setFirstName("Super");
        user.setLastName("Admin");
        String dateTimeStr = "2020-01-01 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        user.setDateJoined(dateTime);
        user.setProfileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg");
        user.setActive(true);
        Mockito.when(redUserService.getUserById(id)).thenReturn(user);
        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mvc.perform(MockMvcRequestBuilders.get(new URI("/users/1"))
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"Successfully retrieved user info\",\n" +
                        "    \"firstName\": \"Super\",\n" +
                        "    \"lastName\": \"Admin\",\n" +
                        "    \"registrationDate\": \"2020-01-01T00:00:00\",\n" +
                        "    \"imageURL\": \"https://forumproject.s3.us-east-2.amazonaws.com/default.jpg\",\n" +
                        "    \"userId\": 1\n" +
                        "}"));
    }

    @Test
    public void testUpdateUserWithError() throws Exception {
        // given
        Long id = 1L;
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setFirstName("Super");
        updateRequest.setLastName("Admin");
        updateRequest.setEmail("super@email.com");
        updateRequest.setPassword("123");
        updateRequest.setProfileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg");

        UserNullable updatedUser = new UserNullable();
        updatedUser.setUserId(1l);
        updatedUser.setFirstName(updateRequest.getFirstName());
        updatedUser.setLastName(updateRequest.getLastName());
        updatedUser.setEmail(updateRequest.getEmail());
        updatedUser.setPassword(updateRequest.getPassword());
        updatedUser.setProfileImageURL(updateRequest.getProfileImageURL());
        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.updateUser(updatedUser)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"User does not exist!\"\n" +
                                "}"));
    }

    @Test
    public void testUpdateUserWithError2() throws Exception {
        // given
        Long id = 1L;
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setFirstName("Super");
        updateRequest.setLastName("Admin");
        updateRequest.setEmail("super@email.com");
        updateRequest.setPassword("123");
        updateRequest.setProfileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg");

        UserNullable updatedUser = new UserNullable();
        Authentication authentication = new TestingAuthenticationToken("user", "password", "normal");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.updateUser(updatedUser)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"You are not authorized to update this content\"\n" +
                        "}"));
    }
    @Test
    public void testdeleteUserWithError() throws Exception {
        // given
        Long id = 1L;
        UserNullable updatedUser = new UserNullable();
        //updatedUser.setUserId(1l);
        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.updateUser(updatedUser)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"User does not exist!\"\n" +
                        "}"));
    }
    @Test
    public void testdeleteUserWithError2() throws Exception {
        // given
        Long id = 1L;
        UserNullable updatedUser = new UserNullable();
        //updatedUser.setUserId(1l);
        Authentication authentication = new TestingAuthenticationToken("user", "password", "normal");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.updateUser(updatedUser)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"You are not authorized to delete this content\"\n" +
                        "}"));
    }
    @Test
    public void testdeleteUserWithoutError() throws Exception {
        // given
        Long id = 1L;
        User updatedUser = new User();
        updatedUser.setUserId(1l);
        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.getUserById(id)).thenReturn(updatedUser);
        Mockito.when(redUserService.deleteUser(updatedUser)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"Success! The User was deleted.\"\n" +
                        "}"));
    }

    @Test
    public void testPromoteUserWithError() throws Exception {
        // given
        Long id = 1L;
        User updatedUser = new User();
        updatedUser.setUserId(1l);
        Authentication authentication = new TestingAuthenticationToken("user", "password", "super");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.promoteUser(updatedUser)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.put("/users/1/promote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlckBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoic3VwZXIifSx7ImF1dGhvcml0eSI6ImFkbWluIn0seyJhdXRob3JpdHkiOiJub3JtYWwifSx7ImF1dGhvcml0eSI6InVudmVyaWZpZWQifV0sImlkIjoxfQ.C8JuQwGBBN9FiJfI-Xan5BB1B3OBL5G1QEAM0RGPv_k")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"User does not exist!\"\n" +
                        "}"));
    }
    @Test
    public void testPromoteUserWithError2() throws Exception {
        // given
        Long id = 1L;
        User updatedUser = new User();
        updatedUser.setUserId(1l);
        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.promoteUser(updatedUser)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.put("/users/1/promote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlckBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoic3VwZXIifSx7ImF1dGhvcml0eSI6ImFkbWluIn0seyJhdXRob3JpdHkiOiJub3JtYWwifSx7ImF1dGhvcml0eSI6InVudmVyaWZpZWQifV0sImlkIjoxfQ.C8JuQwGBBN9FiJfI-Xan5BB1B3OBL5G1QEAM0RGPv_k")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"You are not authorized to promote this user\"\n" +
                        "}"));
    }
    @Test
    public void testPromoteUserWithoutError() throws Exception {
        // given
        Long id = 1L;
        User updatedUser = new User();
        updatedUser.setUserId(1l);
        Authentication authentication = new TestingAuthenticationToken("user", "password", "super");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.getUserById(id)).thenReturn(updatedUser);
        Mockito.when(redUserService.promoteUser(updatedUser)).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.put("/users/1/promote")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlckBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoic3VwZXIifSx7ImF1dGhvcml0eSI6ImFkbWluIn0seyJhdXRob3JpdHkiOiJub3JtYWwifSx7ImF1dGhvcml0eSI6InVudmVyaWZpZWQifV0sImlkIjoxfQ.C8JuQwGBBN9FiJfI-Xan5BB1B3OBL5G1QEAM0RGPv_k")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"Success! The User was promoted.\"\n" +
                        "}"));
    }

    @Test
    public void testproduceDirectWithoutError() throws Exception {
        // given
        Long id = 1L;
        User updatedUser = new User();
        updatedUser.setUserId(1l);

        //Mockito.when(redUserService.getUserById(id)).thenReturn(updatedUser);
        //Mockito.when(redUserService.promoteUser(updatedUser)).thenReturn(true);
        Authentication authentication = new TestingAuthenticationToken(1l, "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mvc.perform(MockMvcRequestBuilders.patch("/users/verify/email")
                        .param("routingKey", "apple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdXBlckBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoic3VwZXIifSx7ImF1dGhvcml0eSI6ImFkbWluIn0seyJhdXRob3JpdHkiOiJub3JtYWwifSx7ImF1dGhvcml0eSI6InVudmVyaWZpZWQifV0sImlkIjoxfQ.C8JuQwGBBN9FiJfI-Xan5BB1B3OBL5G1QEAM0RGPv_k")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"User does not exist!\"\n" +
                        "}"));
    }

    @Test
    public void testUpdateUserWithoutError() throws Exception {
        // given
        Long id = 1L;
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setFirstName("Super");
        updateRequest.setLastName("Admin");
        updateRequest.setEmail("super@email.com");
        updateRequest.setPassword("123");
        updateRequest.setProfileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg");

        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.updateUser(Mockito.any(UserNullable.class))).thenReturn(true);

        mvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"Success! The User was updated.\"\n" +
                        "}"));
    }

    @Test
    public void testUpdateActiveWithError() throws Exception {
        // given
        Long id = 1L;
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setFirstName("Super");
        updateRequest.setLastName("Admin");
        updateRequest.setEmail("super@email.com");
        updateRequest.setPassword("123");
        updateRequest.setProfileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg");

        Authentication authentication = new TestingAuthenticationToken("user", "password", "normal");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Mockito.when(redUserService.updateUserActive(1l, true));

        mvc.perform(MockMvcRequestBuilders.post("/users/1/updateActive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"You are not authorized to update the active status of this user\"\n" +
                        "}"));
    }
    @Test
    public void testUpdateActiveWithError2() throws Exception {
        // given
        Long id = 1L;
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setFirstName("Super");
        updateRequest.setLastName("Admin");
        updateRequest.setEmail("super@email.com");
        updateRequest.setPassword("123");
        updateRequest.setProfileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg");

        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.getUserById(1l)).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.post("/users/1/updateActive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"User does not exist!\"\n" +
                        "}"));
    }
    @Test
    public void testUpdateActiveWithError3() throws Exception {
        // given
        Long id = 1L;
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setFirstName("Super");
        updateRequest.setLastName("Admin");
        updateRequest.setEmail("super@email.com");
        updateRequest.setPassword("123");
        updateRequest.setProfileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg");

        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(redUserService.getUserById(1l)).thenReturn(new User().builder().type(1).build());

        mvc.perform(MockMvcRequestBuilders.post("/users/1/updateActive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"You are not authorized to update the active status of this user\"\n" +
                        "}"));
    }

    @Test
    public void testUpdateActiveWithoutError() throws Exception {
        Long userId = 1L;
        Boolean activeStatus = true;
        User mockUser = new User();
        mockUser.setType(2); // Make sure type is not 0 or 1, as these are forbidden

        UpdateActiveRequest updateActiveRequest = new UpdateActiveRequest();
        updateActiveRequest.setActive(activeStatus);

        Authentication authentication = new TestingAuthenticationToken("user", "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mock UserService methods
        Mockito.when(redUserService.getUserById(userId)).thenReturn(mockUser);
        Mockito.doNothing().when(redUserService).updateUserActive(userId, activeStatus);

        // Perform the request
        mvc.perform(MockMvcRequestBuilders.post("/users/" + userId + "/updateActive")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(updateActiveRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"Success! The User's active status has been updated.\"\n" +
                        "}"));
    }

    @Test
    public void testVerifyCode() throws Exception {
        Long userId = 1L;
        String code = "123456";
        User mockUser = new User();

        CodeRequest codeRequest = new CodeRequest();
        codeRequest.setCode(code);

        // Create authentication
        Authentication authentication = new TestingAuthenticationToken(1l, "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Mock UserService methods
        Mockito.when(redUserService.getUserById(userId)).thenReturn(mockUser);
        Mockito.when(redUserService.verifyCode(mockUser, code)).thenReturn(true);

        // Perform the request
        mvc.perform(MockMvcRequestBuilders.post("/users/verify/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(codeRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"Email verification succeeded!\"\n" +
                        "}"));
    }

    @Test
    public void testVerifyCodeWithError() throws Exception {
        Long userId = 1L;
        String code = "123456";
        User mockUser = new User();

        CodeRequest codeRequest = new CodeRequest();
        codeRequest.setCode(code);

        // Create authentication
        Authentication authentication = new TestingAuthenticationToken(1l, "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Mock UserService methods
        Mockito.when(redUserService.getUserById(userId)).thenReturn(null);
        //Mockito.when(redUserService.verifyCode(mockUser, code)).thenReturn(true);

        // Perform the request
        mvc.perform(MockMvcRequestBuilders.post("/users/verify/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(codeRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"User does not exist! Cannot verify user.\"\n" +
                        "}"));
    }

    @Test
    public void testVerifyCodeWithError2() throws Exception {
        Long userId = 1L;
        String code = "123456";
        User mockUser = new User();

        CodeRequest codeRequest = new CodeRequest();
        codeRequest.setCode(code);

        // Create authentication
        Authentication authentication = new TestingAuthenticationToken(1l, "password", "admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // Mock UserService methods
        Mockito.when(redUserService.getUserById(userId)).thenReturn(mockUser);
        Mockito.when(redUserService.verifyCode(mockUser, code)).thenReturn(false);

        // Perform the request
        mvc.perform(MockMvcRequestBuilders.post("/users/verify/code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBlbWFpbC5jb20iLCJwZXJtaXNzaW9ucyI6W3siYXV0aG9yaXR5Ijoibm9ybWFsIn0seyJhdXRob3JpdHkiOiJhZG1pbiJ9XSwiaWQiOjJ9.A7t2VzXXt7eN_YkBss94yN1wDzYBR-0UryhjH2nOFNI")
                        .content(new ObjectMapper().writeValueAsString(codeRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().json("{\n" +
                        "    \"message\": \"User verification failed!\"\n" +
                        "}"));
    }

    @AfterEach
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

}
