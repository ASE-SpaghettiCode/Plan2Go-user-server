package ASESpaghettiCode.UserServer.Repository;

import ASESpaghettiCode.UserServer.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    public List<User> findAll();
    public User findByUserId(String userId);
    public User findByUsername(String username);
}
