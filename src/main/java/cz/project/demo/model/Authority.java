package cz.project.demo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Table(name = "authority")
@Entity
public class Authority implements GrantedAuthority {

    @Id
    private String id;

    @Id
    private String name;

    public Authority(String name) {
        this.name = name;
    }

    public Authority() {

    }

    @Override
    public String getAuthority() {
        return id;
    }

}
