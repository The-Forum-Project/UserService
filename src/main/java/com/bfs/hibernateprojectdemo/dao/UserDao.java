package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends AbstractHibernateDao<User> {


    @Autowired
    public UserDao() {
        setClazz(User.class);
    }

    public User getUserById(Long id) {
        return this.findById(id);
    }

    public List<User> getAllUsers() {
        return this.getAll();
    }

    public void addUser(User user) {
        this.add(user);
    }

    public void updateUser(User user) {
        this.getCurrentSession().save(user);
    }

    public void updateUserCode(Long userId, String code) {
        Session session = this.getCurrentSession();
        String hql = "UPDATE User SET code = :newCode WHERE userId = :userId";
        Query query = session.createQuery(hql);
        query.setParameter("newCode", code);
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

}
