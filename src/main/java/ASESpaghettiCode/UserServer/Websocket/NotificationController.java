package ASESpaghettiCode.UserServer.Websocket;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Websocket.NotificationModel.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;

    }

    @GetMapping("/notifications/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Notification> getNotifications(@PathVariable String userId) {
        return notificationService.getNotifications(userId);
    }

    @DeleteMapping("/notifications/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotifications(@PathVariable String userId) {
       notificationService.deleteNotificationsByOwnerId(userId);
    }

    @PostMapping("/notifications")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Notification newNotification) {
        notificationService.create(newNotification);
    }

    @PutMapping("/notifications")
    @ResponseStatus(HttpStatus.OK)
    public void putLikeList(@RequestBody Notification notification) {
        System.out.println("delete method is called by");
        System.out.println(notification);
        notificationService.deleteNoteFromLikedList(notification);
    }





}
