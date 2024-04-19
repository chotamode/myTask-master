package cz.project.demo.rest;

import cz.project.demo.model.Message;
import cz.project.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//FIXME message controller
// -GET all
// -POST
// -
@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> getAllMessages(){
        return messageService.getAllMessages();
    }

    @GetMapping("/{username}")
    public List<Message> getMessagesFromSender(@PathVariable String username){
        return messageService.getAllMessagesFromSender(username);
    }

    @PostMapping("/{username}")
    public ResponseEntity<Message> sendMessage(@PathVariable String username, @RequestBody String text){
        messageService.sendMessage(username, text);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{username}/{messageId}")
    public ResponseEntity<Message> redactMessage(@PathVariable Long messageId,
                                                 @PathVariable String username,
                                                 @RequestBody String text){
        messageService.updateMessage(username, messageId, text);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{username}/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId,
                                              @PathVariable String username){
        messageService.deleteMessage(username, messageId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
