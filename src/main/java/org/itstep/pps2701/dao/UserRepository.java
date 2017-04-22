package org.itstep.pps2701.dao;

import org.itstep.pps2701.entities.User;

import javax.persistence.TypedQuery;

public class UserRepository extends AbstractRepository<User, Long> {

    public User findByLogin(String login) {
        TypedQuery<User> typedQuery = em.createQuery("SELECT s FROM User s WHERE s.login = :login", User.class);
        typedQuery.setParameter("login", login);

        return typedQuery.getSingleResult();
    }


}
