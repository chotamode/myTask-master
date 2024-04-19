package cz.project.demo.dao;

import cz.project.demo.model.Role;
import cz.project.demo.model.User;
import org.eclipse.persistence.internal.jpa.rs.metadata.model.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

import static org.eclipse.persistence.jpa.JpaHelper.createQuery;

@Repository
public class RoleDao extends BaseDao<Role>{

    public RoleDao() {
        super(Role.class);
    }

    public boolean exists(String name) {
        try {
            return em.createNamedQuery("Role.findByName", User.class).setParameter("name", name)
                    .getSingleResult() != null;
        } catch (NoResultException e) {
            return false;
        }
    }
}
