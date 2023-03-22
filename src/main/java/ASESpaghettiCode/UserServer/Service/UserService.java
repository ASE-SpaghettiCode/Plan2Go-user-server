package ASESpaghettiCode.UserServer.Service;

import ASESpaghettiCode.UserServer.Model.User;
import ASESpaghettiCode.UserServer.Repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers(){
        return this.userRepository.findAll();
    }
    // register
    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        checkIfUserExists(newUser);
        newUser = userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format(baseErrorMessage, "username", "is"));
        }
    }

    // login
    public User loginUser(User user) {
        user = checkIfPasswordWrong(user);
        user.setToken(UUID.randomUUID().toString());

        return user;
    }

    private User checkIfPasswordWrong(User userToBeLoggedIn) {

        User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Username may not exist!!");
        }
        else if (!userByUsername.getPassword().equals(userToBeLoggedIn.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password Incorrect!");
        }
        else {
            return userByUsername;
        }
    }

    public User getUserById(String userId) {
        Optional<User> checkUser = Optional.ofNullable(userRepository.findByUserId(userId));
        if (checkUser.isPresent()) {
            return checkUser.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!");
        }
    }

    // logout
    public void logoutUser(User user) {
        user.setToken("");
    }

    //edit profile
    public void editUser(User userInput){
        if(!userRepository.existsById(userInput.getUserId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"user does not exists");
        }

        User editeduser=getUserById(userInput.getUserId());

        if(userInput.getUsername().equals(editeduser.getUsername())){
            editeduser.setInfo(userInput.getInfo());
            editeduser.setPassword(userInput.getPassword());
            editeduser.setImageLink(userInput.getImageLink());
        }else if(userRepository.findByUsername(userInput.getUsername())==null){
            editeduser.setUsername(userInput.getUsername());
            editeduser.setInfo(userInput.getInfo());
            editeduser.setPassword(userInput.getPassword());
            editeduser.setImageLink(userInput.getImageLink());
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT,"username exists");
        }

        userRepository.save(editeduser);

    }
}
