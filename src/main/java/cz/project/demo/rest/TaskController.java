package cz.project.demo.rest;

import cz.project.demo.model.AcceptanceMessage;
import cz.project.demo.model.Category;
import cz.project.demo.model.Comment;
import cz.project.demo.model.Task;
import cz.project.demo.service.CategoryService;
import cz.project.demo.service.CommentService;
import cz.project.demo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OrderBy;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CommentService commentService;
    private final CategoryService categoryService;

    @Autowired
    public TaskController(TaskService taskService, CommentService commentService, CategoryService categoryService) {
        this.taskService = taskService;
        this.commentService = commentService;
        this.categoryService = categoryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> createTask(@RequestBody Task task,
                                           @RequestParam("repeatMode") RepeatMode repeatMode,
                                           @RequestParam("quantity") Integer quantity) {

        taskService.createTask(task, repeatMode, quantity);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable(value = "id") Long id) {
        taskService.deleteTask(id);
    }

    @PostMapping("/{id}/acceptance_message")
    public ResponseEntity<Task> sendAcceptanceMessage(@PathVariable(value = "id") Long taskId,
                                                      @RequestBody AcceptanceMessage message) {
        taskService.sendAcceptanceMessage(message, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @return a list of tasks that the user can perform that do not belong to user
     */

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getAllOthersTasks() {
        return taskService.getAllOthersTasks();
    }

    /**
     * @return a list of tasks that belong to user
     */
    @GetMapping(value = "/my_tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getAllMyTasks() {
        return taskService.getAllMyTasks();
    }

    @GetMapping(value = "/{id}/acceptanceMessages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AcceptanceMessage> getAcceptanceMessages(@PathVariable(value = "id") Long taskId) {
        return taskService.getAcceptanceMessages(taskId);
    }

    @GetMapping(value = "/{id}/acceptanceMessages/{message_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AcceptanceMessage getAcceptanceMessage(@PathVariable(value = "id") Long taskId,
                                                  @PathVariable(value = "message_id") Long message_id) {

        taskService.getAcceptanceMessageById(taskId, message_id);

        return taskService.getAcceptanceMessageById(taskId, message_id);
    }

    @PutMapping("/{id}/acceptanceMessages/{message_id}")
    public ResponseEntity<Task> approveMessage(@PathVariable(value = "id") Long taskId,
                                               @PathVariable(value = "message_id") Long message_id) {

        taskService.approveMessage(taskId, message_id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/owner_completed")
    public ResponseEntity<Task> taskCompletedOwner(@PathVariable(value = "id") Long taskId,
                                                   @RequestBody String review,
                                                   @RequestParam("stars") int ownerStars) {

        taskService.taskCompletedOwner(taskId, review, ownerStars);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/performer_completed")
    public ResponseEntity<Task> taskCompletedPerformer(@PathVariable(value = "id") Long taskId,
                                                       @RequestBody String review,
                                                       @RequestParam("stars") int performerStars) {

        taskService.taskCompletedPerformer(taskId, review, performerStars);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getAllComments(@PathVariable(value = "id") Long taskId) {
        return commentService.getAllComments(taskId);
    }

    @GetMapping("/{id}/comments/{comment_id}")
    public Comment getComment(@PathVariable(value = "id") Long taskId, @PathVariable(value = "comment_id") Long commentId) {
        return commentService.getCommentById(taskId, commentId);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> postComment(@PathVariable(value = "id") Long taskId,
                                               @RequestBody String comment) {
        commentService.createComment(comment, taskId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/comments/{comment_id}")
    public void deleteComment(@PathVariable(value = "id") Long taskId,
                              @PathVariable(value = "comment_id") Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<Comment> updateComment(@PathVariable(value = "id") Long taskId,
                                                 @PathVariable(value = "comment_id") Long comment_id,
                                                 @RequestBody String text) {
        commentService.updateComment(comment_id, text);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/category/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getAllTasksByCategory(@PathVariable Long id) {
        return taskService.findAllByCategory(categoryService.find(id));
    }

    @PostMapping(value = "/{id}/categories", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> addTaskToCategory(@PathVariable(value = "id") Long taskId,
                                                  @RequestBody Category category) {

        categoryService.addTask(category, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}/categories/{category_id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> deleteTaskFromCategory(@PathVariable(value = "id") Long taskId,
                                                  @PathVariable(value = "category_id") Long categoryId) {

        categoryService.removeTask(categoryId, taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
