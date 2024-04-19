package cz.project.demo.dao;


import cz.project.demo.model.Comment;
import cz.project.demo.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@Repository
public class CommentDao extends BaseDao<Comment> {

    public CommentDao() {
        super(Comment.class);
    }

    public List<Comment> findAllCommentsByAuthor(User author) {
        try {
            return em.createNamedQuery("Comment.findAllCommentsByAuthor", Comment.class).setParameter("author", author)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Comment> findAllCommentsByDate(Date date) {
        try {
            return em.createNamedQuery("Comment.findAllCommentsByDate", Comment.class).setParameter("date", date)
                    .getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}
