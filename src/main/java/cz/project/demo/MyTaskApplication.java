package cz.project.demo;

import cz.project.demo.dao.RoleDao;
import cz.project.demo.model.Authority;
import cz.project.demo.model.Role;
import cz.project.demo.model.User;
import cz.project.demo.service.RoleService;
import cz.project.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@SpringBootApplication
public class MyTaskApplication {

    public MyTaskApplication(RoleService roleService) {
        this.roleService = roleService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyTaskApplication.class, args);
    }

    final RoleService roleService;

    @Bean
    CommandLineRunner init (UserService userService){

        return args -> {
            User user1 = new User("name1", "surname1", "nickname1", "password1");
            User user2 = new User("name2", "surname2", "nickname2", "password2");
            User user3 = new User("name3", "surname3", "nickname3", "password3");
            User user4 = new User("name4", "surname4", "nickname4", "password4");
            Role adminRole = new Role("ADMIN");
            Role userRole = new Role("USER");
            if(!roleService.exists(adminRole.getName())){
                roleService.persist(adminRole);
            }
            if(!roleService.exists(userRole.getName())){
                roleService.persist(userRole);
            }

            if(!userService.exists(user1.getUsername())){
                user1.addRole(userRole);
                userService.persist(user1);
            }
            if(!userService.exists(user2.getUsername())){
                user2.addRole(userRole);
                userService.persist(user2);
            }
            if(!userService.exists(user3.getUsername())){
                user3.addRole(userRole);
                userService.persist(user3);
            }
            if(!userService.exists(user4.getUsername())){
                user4.addRole(userRole);
                user4.addRole(adminRole);
                userService.persist(user4);
            }
        };
    }

}
