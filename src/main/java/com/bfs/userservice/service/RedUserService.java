package com.bfs.userservice.service;


import com.bfs.userservice.dao.UserDao;
import com.bfs.userservice.domain.User;
import com.bfs.userservice.util.VerificationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RedUserService {

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Transactional
    public Boolean updateUser(User updatedUser) { return userDao.updateUser(updatedUser); }

    @Transactional
    public Boolean deleteUser(User user) { return userDao.deleteUser(user); }

    @Transactional
    public String updateCode(Long userId) {
        String code = VerificationCodeGenerator.generateCode();
        userDao.updateUserCode(userId, code);
        return code;
    }

    @Transactional
    public Boolean updateEmail(Long userId, String email, String code) {
        User user = getUserById(userId);
        if (!user.getCode().equals(code)) {
            return false;
        }
        user.setEmail(email);
        return true;
    }

}
