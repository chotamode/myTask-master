package cz.project.demo.service;

import cz.project.demo.dao.UserDao;
import cz.project.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class UserService{

    private final UserDao dao;

    final PasswordEncoder passwordEncoder;

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public User getCurrentUser() {
        return dao.findByUsername(getCurrentUsername());
    }

    @Autowired
    public UserService(UserDao dao, PasswordEncoder passwordEncoder) {
        this.dao = dao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void persist(User user) {
        Objects.requireNonNull(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        dao.persist(user);
    }

    @Transactional(readOnly = true)
    public boolean exists(String username) {
        return dao.findByUsername(username) != null;
    }

    @Transactional
    public List<User> findAll(){
        return dao.findAll();
    }

    @Transactional
    public User findByUsername(String username){
        return dao.findByUsername(username);
    }

    @Transactional
    public void remove(String username) {
        dao.remove(findByUsername(username));
    }
}
