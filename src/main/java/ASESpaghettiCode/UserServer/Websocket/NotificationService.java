package ASESpaghettiCode.UserServer.Websocket;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import ASESpaghettiCode.UserServer.Websocket.NotificationModel.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    @Autowired
    public NotificationService( NotificationRepository notificationRepository, SimpMessagingTemplate simpMessagingTemplate,UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
    }

    public void create(Notification notification) {
        notificationRepository.save(notification);
        String noteOrPost = notification.getTargetType();
        if (noteOrPost.equals("note")){
            String actorId = notification.getActorId();
            String noteId = notification.getTargetId();
            Optional<User> userOptional = Optional.ofNullable(userRepository.findByUserId(actorId));
            if (userOptional.isPresent()) {
                if (!userOptional.get().getLikedlist().contains(noteId)){
                    // if noteId is not already in the list
                    userOptional.get().addLikedlist(noteId);
                }
                userRepository.save(userOptional.get());
            }
        }
        simpMessagingTemplate.convertAndSend("/mailbox/"+notification.getOwnerId()+"/fetch", notification);
    }


    public void deleteNoteFromLikedList(Notification notification) {
        String noteOrPost = notification.getTargetType();
        if (noteOrPost.equals("note")){
            System.out.printf("noteOrPost.equals(\"note\")");
            String actorId = notification.getActorId();
            String noteId = notification.getTargetId();
            Optional<User> userOptional = Optional.ofNullable(userRepository.findByUserId(actorId));
            if (userOptional.isPresent()) {
                if (userOptional.get().getLikedlist().contains(noteId)){
                    System.out.println("likelist does contained noteId");
                    System.out.println(userOptional.get().getLikedlist());
                    userOptional.get().removeNoteIdFromLikedlist(noteId);

                }
                userRepository.save(userOptional.get());
            }
        }
    }


    public List<Notification> getNotifications(String userId) {
        Optional<List<Notification>> sortedList = Optional.ofNullable(notificationRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdTime")));
        if (sortedList.isEmpty()) {
            return new ArrayList<>();
        }
        sortedList.get().stream().forEach(notification->notification.setActorName(userRepository.findByUserId(notification.getActorId()).getUsername()));
        return sortedList.get();
    }

    public void deleteNotificationsByOwnerId(String userId) {
        List<Notification> notificationList = notificationRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "createdTime"));
        notificationRepository.deleteAll(notificationList);
    }
}
