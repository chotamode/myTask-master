package cz.project.demo.config;

import cz.project.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    final UserService userService;
    final DataSource dataSource;

    public SecurityConfiguration(PasswordEncoder passwordEncoder, UserService userService, DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(passwordEncoder)
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "SELECT username, password, TRUE FROM users WHERE username=?")
                .authoritiesByUsernameQuery(
                        "SELECT username, name FROM users JOIN users_role ON users.id = users_role.user_id JOIN role ON roles_id = role.id WHERE username=?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").hasRole("USER")
                .and().formLogin();
    }

}
