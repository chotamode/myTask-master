package cz.project.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import cz.project.demo.exception.TaskException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NamedQueries({
        //@NamedQuery(name = "Task.findAllTasksWithoutAuthor", query = "SELECT t FROM Task t WHERE not (t.owner = :owner)"), //to do
        @NamedQuery(name = "Task.findAllTasksWithoutAuthor", query = "SELECT t FROM Task t WHERE (t.owner <> :owner) AND t.performer IS NULL"), //AND (t.ownerApprovedCompletion <> true) AND (t.performerApprovedCompletion <> true)
        @NamedQuery(name = "Task.findAllTasksByAuthor", query = "SELECT t FROM Task t WHERE t.owner = :owner"),
        @NamedQuery(name = "Task.findByCategory", query = "SELECT t from Task t WHERE :category MEMBER OF t.categories AND NOT t.removed")
})
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Task() {
    }

    public Task(Task task) {
        this.created = task.created;
        this.owner = task.owner;
        this.name = task.name;
        this.task = task.task;
        this.price = task.price;
        this.removed = task.removed;
        this.streetNumber = task.streetNumber;
        this.streetName = task.streetName;
        this.suburb = task.suburb;
        this.city = task.city;
        this.state = task.state;
        this.postcode = task.postcode;
        this.categories = task.categories;
    }

    @Basic
    @Column(nullable = false)
    protected LocalDateTime created = LocalDateTime.now();
    @Basic
    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime date;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    @OneToOne
    @JoinColumn(name = "performer_id")
    private User performer;

    @Basic
    @Column(nullable = false)
    private String name;
    @Basic
    @Column(nullable = false)
    private String task;
    @Basic
    @Column(nullable = false)
    private Integer price;
    @Basic
    @Column(nullable = false)
    private boolean removed = false;

    @Basic
    @Column(nullable = false)
    private boolean ownerApprovedCompletion = false;

    @Basic
    @Column(nullable = false)
    private boolean performerApprovedCompletion = false;

    @Basic
    @Column
    String performerReview;
    @Basic
    @Column
    String ownerReview;
    @Basic
    @Column
    Integer performerStars;
    @Basic
    @Column
    Integer ownerStars;

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

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<Comment>();

    @ManyToMany
    private List<Category> categories;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AcceptanceMessage> acceptanceMessages;

    public void addAcceptanceMessage(AcceptanceMessage message){
        acceptanceMessages.add(message);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }


    public void setPerformerStars(Integer performerStars) {
        if(performerStars > 5 || performerStars < 0){
            throw new TaskException("Can't rate <0 and >5");
        }
        this.performerStars = performerStars;
    }

    public void setOwnerStars(Integer ownerStars) {
        if(ownerStars > 5 || ownerStars < 0){
            throw new TaskException("Can't rate <0 and >5");
        }
        this.ownerStars = ownerStars;
    }
}
