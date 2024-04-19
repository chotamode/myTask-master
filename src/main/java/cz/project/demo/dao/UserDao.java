package cz.project.demo.dao;

import cz.project.demo.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UserDao extends BaseDao<User> {

    public UserDao() {
        super(User.class);
    }

    public User findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", User.class).setParameter("nickname", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<User> findAll() {
        try {
            return em.createNamedQuery("User.findAll", User.class).getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
}
