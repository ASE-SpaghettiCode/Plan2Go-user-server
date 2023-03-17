package ASESpaghettiCode.UserServer.Service;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void save(User user){
        userRepository.save(user);
    }

}
