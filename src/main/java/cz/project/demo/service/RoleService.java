package cz.project.demo.service;

import cz.project.demo.dao.RoleDao;
import cz.project.demo.exception.NotFoundException;
import cz.project.demo.model.Role;
import cz.project.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class RoleService {

    private final RoleDao dao;

    @Autowired
    public RoleService(RoleDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void persist(Role role) {
        Objects.requireNonNull(role);
        dao.persist(role);
    }

    @Transactional void remove(Long id) {
        Role role = dao.find(id);
        if(role == null){
            throw new NotFoundException("Role id = " + id + "not found");
        }
        dao.remove(role);
    }

    public boolean exists(String id) {
        return dao.exists(id);
    }
}
