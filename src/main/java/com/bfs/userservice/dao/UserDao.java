package com.bfs.userservice.dao;

import com.bfs.userservice.domain.User;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.beans.PropertyDescriptor;

@Repository
public class UserDao extends AbstractHibernateDao<User> {


    @Autowired
    public UserDao() {
        setClazz(User.class);
    }

    public User getUserById(Long id) {
        return this.findById(id);
    }

    public Boolean updateUser(User updatedUser) {

        // Load the existing user
        User existingUser = this.getCurrentSession().get(User.class, updatedUser.getUserId());

        if (existingUser != null) {
            BeanWrapper existingUserWrapper = new BeanWrapperImpl(existingUser);
            BeanWrapper newUserWrapper = new BeanWrapperImpl(updatedUser);

            for (PropertyDescriptor descriptor : existingUserWrapper.getPropertyDescriptors()) {
                String propertyName = descriptor.getName();

                Object oldValue = existingUserWrapper.getPropertyValue(propertyName);
                Object newValue = newUserWrapper.getPropertyValue(propertyName);

                if (newValue == null) {
                    continue;
                }

                if (oldValue != null && newValue != null && !oldValue.equals(newValue)) {
                    existingUserWrapper.setPropertyValue(propertyName, newValue);
                }
            }

            return true;

        } else {

            return false;

        }
    }

    public Boolean deleteUser(User userToDelete) {

        if (userToDelete == null) {

            return false;

        } else {

            userToDelete.setActive(false);
            this.getCurrentSession().update(userToDelete);

            return true;

        }

    }

    public Boolean promoteUser(User userToPromote) {

        if (userToPromote == null) {

            return false;

        } else {

            userToPromote.setType(1);
            this.getCurrentSession().update(userToPromote);

            return true;

        }

    }

    public Boolean verifyUser(User userToPromote) {

        if (userToPromote == null) {

            return false;

        } else {

            if (userToPromote.getType() != 3) {
                return false;
            }

            userToPromote.setType(2);
            this.getCurrentSession().update(userToPromote);

            return true;

        }

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
