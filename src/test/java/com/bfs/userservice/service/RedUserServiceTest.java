package com.bfs.userservice.service;

import com.bfs.userservice.dao.UserDao;
import com.bfs.userservice.domain.User;
import com.bfs.userservice.dto.UserNullable;
import com.bfs.userservice.util.VerificationCodeGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
public class RedUserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private RedUserService redUserService;

    @Mock
    VerificationCodeGenerator verificationCodeGenerator;

    private User testUser;
    private UserNullable testUserNullable;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        String dateTimeStr = "2020-01-01 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        testUser = new User().builder().userId(1l).firstName("Super").lastName("Admin")
                .email("super@email.com").password("123").active(true)
                .dateJoined(dateTime).type(0).code("0")
                .profileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg").build();
        testUserNullable = new UserNullable().builder().userId(1l).firstName("Super").lastName("Admin")
                .email("super@email.com").password("123")
                .profileImageURL("https://forumproject.s3.us-east-2.amazonaws.com/default.jpg").build();
    }

    @Test
    public void testGetUserById() {
        Mockito.when(userDao.getUserById(1L)).thenReturn(testUser);

        User result = redUserService.getUserById(1L);

        assertEquals(testUser, result);
    }

    @Test
    public void testUpdateUser() {
        Mockito.when(userDao.updateUser(Mockito.any(UserNullable.class))).thenReturn(true);

        Boolean result = redUserService.updateUser(testUserNullable);

        assertTrue(result);
    }

    @Test
    public void testPromoteUser() {
        //User testUser = new User();
        Mockito.when(userDao.promoteUser(Mockito.any(User.class))).thenReturn(true);

        Boolean result = redUserService.promoteUser(testUser);

        assertTrue(result);
        Mockito.verify(userDao, Mockito.times(1)).promoteUser(testUser);
    }

    @Test
    public void testUpdateCode() {
        String code = "0";
        Mockito.when(userDao.verifyUser(Mockito.any(User.class))).thenReturn(true);
        Boolean result = redUserService.verifyCode(testUser, code);
        assertTrue(result);
        Mockito.verify(userDao, Mockito.times(1)).verifyUser(testUser);
    }
}







