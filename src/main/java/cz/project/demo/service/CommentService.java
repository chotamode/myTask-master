package cz.project.demo.service;

import cz.project.demo.dao.CommentDao;
import cz.project.demo.dao.TaskDao;
import cz.project.demo.dao.UserDao;
import cz.project.demo.exception.CommentException;
import cz.project.demo.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    private final CommentDao commentDao;
    private final UserDao userDao;
    private final TaskDao taskDao;
    private final UserService userService;

    @Autowired
    public CommentService(CommentDao commentDao, UserDao userDao, TaskDao taskDao, UserService userService) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.taskDao = taskDao;
        this.userService = userService;
    }

    @Transactional
    public void createComment(String text, Long taskId) {
        Comment comment = new Comment();
        comment.setComment(text);
        comment.setAuthor(userDao.findByUsername(userService.getCurrentUsername()));
        comment.setTask(taskDao.find(taskId));
        taskDao.find(taskId).addComment(comment);
        commentDao.persist(comment);
    }

    @Transactional
    public void updateComment(Long comment_id, String text){
        Comment comment = commentDao.find(comment_id);
        if(comment.getAuthor() == userDao.findByUsername(userService.getCurrentUsername())){
            comment.setComment(text);
            commentDao.update(comment);
        }else{
            throw new CommentException("You are not allowed to redact this comment");
        }
    }

    @Transactional
    public void deleteComment(Long comment_id){
        Comment comment = commentDao.find(comment_id);
        if(comment.getAuthor() == userDao.findByUsername(userService.getCurrentUsername()) ||
                comment.getTask().getOwner() == userDao.findByUsername(userService.getCurrentUsername())){
            commentDao.remove(comment);
        }else{
            throw new CommentException("You are not allowed to delete this comment");
        }
    }

    @Transactional
    public List<Comment> getAllComments(Long taskId) {
        return taskDao.find(taskId).getComments();
    }

    @Transactional
    public Comment getCommentById(Long taskId, Long comment_id){
        return getAllComments(taskId).stream()
                .filter(c -> c.getId().equals(comment_id))
                .findAny()
                .orElseThrow(() -> new CommentException("No such comment"));
    }
}
