package ASESpaghettiCode.UserServer.Websocket;

import ASESpaghettiCode.UserServer.Websocket.NotificationModel.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    @Query("{ownerId: ?0}")
    List<Notification> findByUserId(String userId, Sort sort);

    @Query("{ownerId: ?0 }")
    void deleteByOwnerId(String ownerId);
}
