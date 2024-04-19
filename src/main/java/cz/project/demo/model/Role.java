package cz.project.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NamedQueries({
        @NamedQuery(name = "Role.findByName", query = "SELECT r FROM Role r WHERE r.name = :name")
})
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @Column(unique = true)
    private String name;

    public Role(String name) {
        this.name = "ROLE_" + name;
    }

    public Role() {

    }

    @ManyToMany
    @JoinTable(name = "authority")
    private final List<Authority> authorities = new ArrayList<>();

    @Override
    public String getAuthority() {
        return name;
    }

    public void addAuthority(Authority authority){
        authorities.add(authority);
    }
}
