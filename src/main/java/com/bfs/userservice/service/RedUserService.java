package com.bfs.userservice.service;


import com.bfs.userservice.dao.UserDao;
import com.bfs.userservice.domain.User;
import com.bfs.userservice.util.VerificationCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional
    public Boolean updateUser(User updatedUser) { return userDao.updateUser(updatedUser); }

    @Transactional
    public Boolean deleteUser(User user) { return userDao.deleteUser(user); }

    @Transactional
    public Boolean promoteUser(User user) { return userDao.promoteUser(user); }

    @Transactional
    public String updateCode(Long userId) {
        String code = VerificationCodeGenerator.generateCode();
        userDao.updateUserCode(userId, code);
        return code;
    }

    @Transactional
    public Boolean verifyCode(User user, String code) {

        if (!user.getCode().equals(code)) {
            return false;
        }

        return userDao.verifyUser(user);

    }

}
