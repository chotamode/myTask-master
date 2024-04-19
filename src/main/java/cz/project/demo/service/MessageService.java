package cz.project.demo.service;

import cz.project.demo.dao.MessageDao;
import cz.project.demo.model.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    private final MessageDao messageDao;
    private final UserService userService;

    public MessageService(MessageDao messageDao, UserService userService) {
        this.messageDao = messageDao;
        this.userService = userService;
    }

    @Transactional
    public void persist(Message message) {
        messageDao.persist(message);
    }

    @Transactional
    public List<Message> getAllMessages() {
        return messageDao.findAll(userService.getCurrentUser());
    }

    @Transactional
    public List<Message> getAllMessagesFromSender(String sender) {
        return messageDao.findAllFromSender(userService.getCurrentUser(), userService.findByUsername(sender));
    }

    @Transactional
    public void sendMessage(String username, String text) {
        Message message = new Message();
        message.setSender(userService.getCurrentUser());
        message.setReceiver(userService.findByUsername(username));
        message.setContent(text);
        messageDao.persist(message);
    }

    @Transactional
    public void updateMessage(String username, Long messageId, String text) {
        Message message = messageDao.find(messageId);
        message.setContent(text);
        messageDao.update(message);
    }

    @Transactional
    public void deleteMessage(String username, Long messageId) {
        messageDao.remove(messageDao.find(messageId));
    }
}
