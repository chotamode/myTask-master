package cz.project.demo.service;

import cz.project.demo.dao.AcceptanceMessageDao;
import cz.project.demo.dao.TaskDao;
import cz.project.demo.dao.UserDao;
import cz.project.demo.exception.AuthorizationException;
import cz.project.demo.exception.NotFoundException;
import cz.project.demo.exception.TaskException;
import cz.project.demo.model.AcceptanceMessage;
import cz.project.demo.model.Category;
import cz.project.demo.model.Task;
import cz.project.demo.rest.RepeatMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OrderBy;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskDao taskDao;
    private final AcceptanceMessageDao acceptanceMessageDao;
    private final UserDao userDao;
    private final UserService userService;

    @Autowired
    public TaskService(TaskDao taskDao, AcceptanceMessageDao acceptanceMessageDao, UserDao userDao, UserService userService) {
        this.taskDao = taskDao;
        this.acceptanceMessageDao = acceptanceMessageDao;
        this.userDao = userDao;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<Task> findAll() {
        return taskDao.findAll();
    }

    @Transactional(readOnly = true)
    public List<Task> findAllByCategory(Category category) {
        return taskDao.findAllByCategory(category);
    }

    @Transactional(readOnly = true)
    public Task find(Long id) {
        return taskDao.find(id);
    }

    @Transactional
    public void persist(Task task) {
        taskDao.persist(task);
    }

    @Transactional
    public void updateTask(Task task) {
        taskDao.update(task);
    }

    @Transactional
    public void deleteTask(Long id) {
//        for (int i=0; i < userService.getCurrentUser().getRoles().size(); i++){
            if (userService.getCurrentUser().isAdmin()) { //.getRoles().get(i).toString().equals("ROLE_ADMIN")
                taskDao.remove(taskDao.find(id));
            }
//        }
        if (taskDao.find(id).getOwner() == userService.getCurrentUser()){
            taskDao.remove(taskDao.find(id));
        }else throw new TaskException("you can't delete this task");
    }

    @Transactional
    public void addAcceptanceMessage(AcceptanceMessage message, Task task){
        acceptanceMessageDao.persist(message);
        task.addAcceptanceMessage(message);
        taskDao.update(task);
    }

    @Transactional
    public void approveMessage(Long taskId, Long messageId){
        Task task = taskDao.find(taskId);
        AcceptanceMessage message = acceptanceMessageDao.find(messageId);

        Objects.requireNonNull(task.getOwner());

        if(!task.getOwner().getUsername().equals(userService.getCurrentUsername())){
            throw new AuthorizationException("Only owner can see acceptanceMessages");
        }

        task.setPerformer(message.getSender());
        taskDao.update(task);
    }

    @Transactional
    public List<AcceptanceMessage> getAcceptanceMessages(Long taskId){
        Task task = taskDao.find(taskId);

        Objects.requireNonNull(task.getOwner());

        if(!task.getOwner().getUsername().equals(userService.getCurrentUsername())){
            throw new AuthorizationException("Only owner can see acceptanceMessages");
        }

        return taskDao.find(taskId).getAcceptanceMessages();
    }

    @Transactional
    public AcceptanceMessage getAcceptanceMessageById(Long taskId, Long messageId) {
        Task task = taskDao.find(taskId);

        AcceptanceMessage acceptanceMessage = getAcceptanceMessages(taskId).get(Math.toIntExact(messageId));

        if(acceptanceMessage == null){
            throw new NotFoundException("Acceptance message not found");
        }

        return getAcceptanceMessages(taskId).stream()
                .filter(c -> c.getId().equals(messageId))
                .findAny()
                .orElseThrow(() -> new TaskException("Acceptance message not found"));
    }

    @Transactional
    public void sendAcceptanceMessage(AcceptanceMessage message, Long taskId){
        Task task = taskDao.find(taskId);
        message.setSender(userDao.findByUsername(userService.getCurrentUsername()));

        if(message.getSender() == task.getOwner()){
            throw new TaskException("message.getSender() == task.getOwner()");
        }

        addAcceptanceMessage(message, task);
    }

    @Transactional
    public void createTask(Task task, RepeatMode repeatMode, Integer quantity){
        Task repeat_task = task;
        if(repeatMode == RepeatMode.NORMAL && quantity != 1){
            throw new TaskException("Bad value in quantity");
        }
        repeat_task.setOwner(userDao.findByUsername(userService.getCurrentUsername()));

        switch (repeatMode) {
            case NORMAL -> {
                taskDao.persist(repeat_task);
            }
            case EVERYWEEK -> {
                if (repeat_task.getDate() == null) {
                    throw new TaskException("Enter start date");
                }
                taskDao.persist(repeat_task);
                for (int i = 0; i < quantity - 1; i++) {
                    repeat_task = new Task(repeat_task);
                    repeat_task.setDate(repeat_task.getDate().plusWeeks(1));
                    taskDao.persist(repeat_task);
                }

            }
            case EVERYDAY -> {
                if (repeat_task.getDate() == null) {
                    throw new TaskException("Enter start date");
                }
                taskDao.persist(repeat_task);
                for (int i = 0; i < quantity - 1; i++) {
                    repeat_task = new Task(repeat_task);
                    repeat_task.setDate(repeat_task.getDate().plusDays(1));
                    taskDao.persist(repeat_task);

                }
            }
        }
    }

    @Transactional
    public void taskCompletedPerformer(Long taskId, String review, Integer performerStars){
        Task task = taskDao.find(taskId);

        if(task.getPerformer() == null|| !task.getPerformer().getUsername().equals(userService.getCurrentUsername())){
            throw new AuthorizationException("Only owner or performer can do completion approval");
        }

        task.setPerformerApprovedCompletion(true);
        task.setPerformerStars(performerStars);
        taskDao.update(task);
    }

    @Transactional
    public void taskCompletedOwner(Long taskId, String review, Integer ownerStars){
        Task task = taskDao.find(taskId);

        if(!task.getOwner().getUsername().equals(userService.getCurrentUsername())){
            throw new AuthorizationException("Only owner or performer can do completion approval");
        }

        task.setOwnerApprovedCompletion(true);
        task.setOwnerStars(ownerStars);
        taskDao.update(task);
    }

    @Transactional
    public List<Task>   getAllOthersTasks(){
        return taskDao.findAllTasksWithoutAuthor(userService.getCurrentUser());
    }


    @Transactional
    public List<Task> getAllMyTasks(){

        return taskDao.findAllTasksByAuthor(userService.getCurrentUser());
    }

    @Transactional
    public String role(){
        return userDao.findByUsername(userService.getCurrentUsername()).getUsername();
    }

}
