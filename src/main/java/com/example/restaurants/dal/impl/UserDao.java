package com.example.restaurants.dal.impl;

import com.example.restaurants.dal.IUserDao;
import com.example.restaurants.data.models.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class UserDao extends BaseDao<User> implements IUserDao {

    public UserDao() {
        super.setCls(User.class);
    }

    @Transactional(readOnly = true)
    @Override
    public User
    getUnique(String email) {

        TypedQuery<User> query =
            entityManager.createQuery(
                "select u from User u where u.email = :email",
                User.class
            );

        query.setParameter("email", email);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
