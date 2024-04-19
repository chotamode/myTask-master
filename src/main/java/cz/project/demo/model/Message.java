package cz.project.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Message.findBySenderReceiver",
                query = "SELECT m FROM Message m WHERE m.sender.username = :sender AND m.receiver.username = :receiver"),
        @NamedQuery(name = "Message.findAll", query = "SELECT m FROM Message m WHERE m.receiver.username = :receiver")
})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Basic
    @Column(nullable = false)
    private String content;

    @Basic
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

}
