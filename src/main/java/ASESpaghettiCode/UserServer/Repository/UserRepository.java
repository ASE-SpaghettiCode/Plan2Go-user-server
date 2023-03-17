package ASESpaghettiCode.UserServer.Repository;


import ASESpaghettiCode.UserServer.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {

}
