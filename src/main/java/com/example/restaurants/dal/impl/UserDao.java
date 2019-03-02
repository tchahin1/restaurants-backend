package com.example.restaurants.dal.impl;

import com.example.restaurants.dal.IUserDao;
import com.example.restaurants.data.models.Users;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

@Repository
public class UserDao extends BaseDao<Users> implements IUserDao {

    public UserDao() {
        super.setCls(Users.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Users
    getUnique(String email) {

        TypedQuery<Users> query =
            entityManager.createQuery(
                "select u from User u where u.email = :email",
                Users.class
            );

        query.setParameter("email", email);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
