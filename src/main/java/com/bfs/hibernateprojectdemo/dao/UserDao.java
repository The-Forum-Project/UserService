package com.bfs.hibernateprojectdemo.dao;

import com.bfs.hibernateprojectdemo.domain.User;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<User> loadUserByEmail(String email){
        String hql = "FROM User u WHERE u.email = :email";
        Query query = getCurrentSession().createQuery(hql, User.class);
        query.setParameter("email", email);
        return Optional.ofNullable((User) query.getSingleResult());
    }

}
