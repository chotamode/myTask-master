package cz.project.demo.dao;

import cz.project.demo.model.Message;
import cz.project.demo.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDao extends BaseDao<Message> {
    public MessageDao() {
        super(Message.class);
    }

    public List<Message> findAll(User receiver) {
        return em.createNamedQuery("Message.findAll", Message.class)
                .setParameter("receiver", receiver)
                .getResultList();
    }

    public List<Message> findAllFromSender(User receiver, User sender) {
        return em.createNamedQuery("Message.findBySenderReceiver", Message.class)
                .setParameter("receiver", receiver)
                .setParameter("sender", sender)
                .getResultList();
    }
}
