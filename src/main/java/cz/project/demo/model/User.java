package cz.project.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :nickname"),
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
})
public class User{

    @Id()
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Basic
    @Column
    private String firstName;

    @Basic
    @Column
    private String lastName;

    @Basic
    @Column(nullable = false, unique = true)
    private String username;

    @Basic(optional = false)
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Basic
    @Column
    private Integer streetNumber;
    @Basic
    @Column
    private String streetName;
    @Basic
    @Column
    private String suburb;
    @Basic
    @Column
    private String city;
    @Basic
    @Column
    private String state;
    @Basic
    @Column
    private Integer postcode;

    @ManyToMany
    @JoinColumn
    @JsonIgnore
    private List<Role> roles;

    public User() {
    }

    public User(String firstName, String lastName, String nickname, String password) {
        this.roles = new ArrayList<Role>();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = nickname;
        this.password = password;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean isAdmin(){
        for (Role role: roles) {
            if (role.toString().equals("ROLE_ADMIN")){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "User{" +
                firstName + " " + lastName +
                "(" + username + ")}";
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<Authority> list = new ArrayList<>();
        for (Role g:
             roles) {
            list.addAll(g.getAuthorities());
        }
        return list;
    }

}
