package cz.project.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class AcceptanceMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Basic
    @Column
    private String message;

    @Basic(optional = false)
    @Column(nullable = false)
    private Integer price;

}
